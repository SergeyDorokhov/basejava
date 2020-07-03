package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.*;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private final SqlHelper helper;
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private static final String INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
    private static final String SELECT_RESUME = "SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid LEFT JOIN section s ON r.uuid = s.resume_uuid WHERE c.resume_uuid = ?";
    private static final String DELETE_RESUME = "DELETE FROM resume WHERE uuid =?";
    private static final String DELETE_RESUMES = "DELETE FROM resume";
    private static final String UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid=?";
    private static final String COUNT_RESUMES = "SELECT COUNT(*) from resume";
    private static final String SELECT_RESUMES_WITH_CONTACTS = "SELECT * FROM resume r \n" +
            "LEFT JOIN contact c ON r.uuid = c.resume_uuid ORDER BY full_name, uuid";
    private static final String SELECT_RESUMES = "SELECT * FROM resume ORDER BY full_name, uuid";
    private static final String SELECT_CONTACT = "SELECT * FROM contact WHERE resume_uuid = ?";
    private static final String SELECT_SECTION = "SELECT * FROM section WHERE resume_uuid = ?";
    private static final String DELETE_CONTACTS = "DELETE FROM contact WHERE resume_uuid=?";
    private static final String DELETE_SECTIONS = "DELETE FROM section WHERE resume_uuid=?";

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.connectAndQuery(DELETE_RESUMES, ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        helper.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(INSERT_RESUME)) {
                helper.setParam(ps, resume.getUuid(), resume.getFullName());
                ps.execute();
            }
            saveContacts(conn, resume);
            saveSections(conn, resume);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return helper.connectAndQuery(SELECT_RESUME, ps -> {
            try (ResultSet result = helper.setParam(ps, uuid).executeQuery()) {
                if (!result.next()) {
                    LOG.warning("Resume " + uuid + " not exist");
                    throw new NotExistStorageException(uuid);
                }
                Resume resume = new Resume(uuid, result.getString("full_name"));
                HashSet<String> set = new HashSet<>();
                do {
                    addContact(result, resume, set);
                    addSection(result, resume, set);
                } while (result.next());
                return resume;
            }
        });
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        helper.executeTransaction(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(UPDATE_RESUME)) {
                helper.setParam(ps, resume.getFullName(), resume.getUuid());
                helper.executeStatement(ps, resume.getUuid());
            }
            delete(conn, resume);
            saveContacts(conn, resume);
            saveSections(conn, resume);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        helper.connectAndQuery(DELETE_RESUME, ps -> {
            helper.executeStatement(helper.setParam(ps, uuid), uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.executeTransaction(conn -> {
            Map<String, Resume> map = new LinkedHashMap<>();
            try (PreparedStatement selectResumes = conn.prepareStatement(SELECT_RESUMES)) {
                ResultSet resumes = selectResumes.executeQuery();
                while (resumes.next()) {
                    String uuid = resumes.getString("uuid").trim();
                    Resume resume = new Resume(uuid, resumes.
                            getString("full_name"));
                    HashSet<String> set = new HashSet<>();
                    addAttribute(conn, SELECT_CONTACT, resume, set);
                    addAttribute(conn, SELECT_SECTION, resume, set);
                    map.put(uuid, resume);
                }
            }
            return new ArrayList<>(map.values());
        });
    }

    private void addAttribute(Connection conn, String sql, Resume resume, HashSet<String> set) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            helper.setParam(ps, resume.getUuid());
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                switch (sql) {
                    case SELECT_CONTACT:
                        addContact(res, resume, set);
                        break;
                    case SELECT_SECTION:
                        addSection(res, resume, set);
                }
            }
        }
    }

    @Override
    public int size() {
        return helper.connectAndQuery(COUNT_RESUMES, ps -> {
            try (ResultSet res = ps.executeQuery()) {
                return res.next() ? res.getInt("COUNT") : 0;
            }
        });
    }

    public void addContact(ResultSet result, Resume resume, Set<String> set) throws SQLException {
        String contact = result.getString("value");
        if (contact != null && !set.contains(contact)) {
            set.add(contact);
            ContactType contactType = ContactType.valueOf(result.getString("type"));
            resume.addContact(contactType, contact);
        }
    }

    private void addSection(ResultSet result, Resume resume, Set<String> set) throws SQLException {
        String content = result.getString("value_section");
        if (content != null && !set.contains(content)) {
            set.add(content);
            SectionType sectionType = SectionType.valueOf(result.getString("type_section"));
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    resume.addSection(sectionType, new TextSection(content));
                    break;
                case QUALIFICATIONS:
                case ACHIEVEMENT:
                    String[] split = content.split("\n");
                    for (int i = 1; i < split.length; i++) {
                        split[i] = "\n" + split[i];
                    }
                    resume.addSection(sectionType, new ListSection(Arrays.asList(split)));
                    break;
                default:
                    break;
            }
        }
    }

    public void saveContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid)" +
                "VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                helper.setParam(ps, entry.getValue(), entry.getKey().name(), resume.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO section (value_section, type_section, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                helper.setParam(ps, String.valueOf(entry.getValue()), entry.getKey().name(), resume.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void delete(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact where resume_uuid = ?;" +
                "DELETE FROM section where resume_uuid = ?")) {
            helper.setParam(ps, resume.getUuid(), resume.getUuid());
            ps.execute();
        }
    }
}