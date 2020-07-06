package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.*;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private final SqlHelper helper;
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private static final String INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
    private static final String SELECT_RESUME = "SELECT * FROM resume r LEFT JOIN contact c " +
            "ON r.uuid = c.resume_uuid LEFT JOIN section s ON r.uuid = s.resume_uuid WHERE r.uuid = ?";
    private static final String SELECT_RESUMES = "SELECT * FROM resume ORDER BY full_name, uuid";
    private static final String DELETE_RESUME = "DELETE FROM resume WHERE uuid =?";
    private static final String DELETE_RESUMES = "DELETE FROM resume";
    private static final String UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid=?";
    private static final String COUNT_RESUMES = "SELECT COUNT(*) from resume";
    private static final String SELECT_CONTACT = "SELECT * FROM contact WHERE resume_uuid = ?";
    private static final String DELETE_CONTACTS = "DELETE FROM contact where resume_uuid =?";
    private static final String SELECT_SECTION = "SELECT * FROM section WHERE resume_uuid = ?";
    private static final String DELETE_SECTIONS = "DELETE FROM section where resume_uuid =?";
    private static final String SELECT_RESUMES_WITH_ATTRIBUTES = "SELECT * FROM resume r LEFT JOIN contact c " +
            "ON r.uuid = c.resume_uuid LEFT JOIN section s " +
            "ON r.uuid = s.resume_uuid ORDER BY full_name, uuid";

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new StorageException(e);
        }
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
            insertContactsDB(conn, resume);
            insertSectionsDB(conn, resume);
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
                do {
                    addContact(result, resume);
                    addSection(result, resume);
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
            deleteAttribute(conn, resume, DELETE_CONTACTS);
            deleteAttribute(conn, resume, DELETE_SECTIONS);
            insertContactsDB(conn, resume);
            insertSectionsDB(conn, resume);
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
            try (PreparedStatement ps = conn.prepareStatement(SELECT_RESUMES_WITH_ATTRIBUTES)) {
                try (ResultSet res = ps.executeQuery()) {
                    while (res.next()) {
                        String uuid = res.getString("uuid").trim();
                        String name = res.getString("full_name");
                        Resume resume = map.computeIfAbsent(uuid, k -> new Resume(uuid, name));
                        addContact(res, resume);
                        addSection(res, resume);
                    }
                }
            }
            return new ArrayList<>(map.values());
        });
    }

    @Override
    public int size() {
        return helper.connectAndQuery(COUNT_RESUMES, ps -> {
            try (ResultSet res = ps.executeQuery()) {
                return res.next() ? res.getInt("COUNT") : 0;
            }
        });
    }

    public void addContact(ResultSet result, Resume resume) throws SQLException {
        String contact = result.getString("value");
        if (contact != null && !resume.getContacts().containsValue(contact)) {
            ContactType contactType = ContactType.valueOf(result.getString("type"));
            resume.addContact(contactType, contact);
        }
    }

    private void addSection(ResultSet result, Resume resume) throws SQLException {
        String value = result.getString("value_section");
        if (value != null) {
            SectionType sectionType = SectionType.valueOf(result.getString("type_section"));
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE:
                    resume.addSection(sectionType, new TextSection(value));
                    break;
                case QUALIFICATIONS:
                case ACHIEVEMENT:
                    String[] split = value.split("\n");
                    resume.addSection(sectionType, new ListSection(Arrays.asList(split)));
                    break;
                default:
                    break;
            }
        }
    }

    public void insertContactsDB(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid)" +
                "VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                helper.setParam(ps, entry.getValue(), entry.getKey().name(), resume.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSectionsDB(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO section (value_section, type_section, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                SectionType sectionType = entry.getKey();
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        helper.setParam(ps, ((TextSection) entry.getValue()).getData(), entry.getKey().name(), resume.getUuid());
                        ps.addBatch();
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> records = ((ListSection) entry.getValue()).getData();
                        StringBuilder builder = new StringBuilder();
                        for (String record : records) {
                            builder.append(record).append("\n");
                        }
                        helper.setParam(ps, builder.toString(), entry.getKey().name(), resume.getUuid());
                        ps.addBatch();
                        break;
                    default:
                        break;
                }
            }
            ps.executeBatch();
        }
    }

    public void deleteAttribute(Connection conn, Resume resume, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            helper.setParam(ps, resume.getUuid());
            ps.execute();
        }
    }
}