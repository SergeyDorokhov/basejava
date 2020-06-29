package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.ContactType;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private final SqlHelper helper;
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private static final String INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
    private static final String SELECT_RESUME = "SELECT * FROM resume r LEFT JOIN contact c " +
            "ON r.uuid = c.resume_uuid WHERE r.uuid =?";
    private static final String SELECT_RESUMES = "SELECT * FROM resume ORDER BY full_name, uuid";
    private static final String DELETE_RESUME = "DELETE FROM resume WHERE uuid =?";
    private static final String DELETE_RESUMES = "DELETE FROM resume";
    private static final String UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid=?";
    private static final String COUNT_RESUMES = "SELECT COUNT(*) from resume";
    private static final String INSERT_CONTACT = "INSERT INTO contact (type, value, resume_uuid) VALUES (?,?,?)";
    private static final String SELECT_CONTACT = "SELECT * FROM contact c where resume_uuid =?";
    private static final String UPDATE_CONTACTS = "UPDATE contact SET value =? WHERE type =? and resume_uuid=?";
    private static final String SELECT_RESUMES_WITH_CONTACTS = "SELECT * FROM resume r \n" +
            "LEFT JOIN contact c ON r.uuid = c.resume_uuid ORDER BY full_name, uuid";

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
                helper.doStatement(ps, resume.getUuid(), resume.getFullName()).execute();
            }
            try (PreparedStatement ps = conn.prepareStatement(INSERT_CONTACT)) {
                helper.doOverAllContacts(ps, resume, entry -> helper
                        .doStatement(ps, entry.getKey().name(), entry.getValue(), resume.getUuid()));
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return helper.connectAndQuery(SELECT_RESUME, ps -> {
            try (ResultSet result = helper.doStatement(ps, uuid).executeQuery()) {
                if (!result.next()) {
                    LOG.warning("Resume " + uuid + " not exist");
                    throw new NotExistStorageException(uuid);
                }
                Resume resume = new Resume(uuid, result.getString("full_name"));
                do {
                    addContact(result, resume);
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
                helper.doStatement(ps, resume.getFullName(), resume.getUuid());
                executeStatement(ps, resume.getUuid());
            }
            try (PreparedStatement ps = conn.prepareStatement(UPDATE_CONTACTS)) {
                helper.doOverAllContacts(ps, resume, entry -> helper
                        .doStatement(ps, entry.getValue(), entry.getKey().name(), resume.getUuid()));
            }
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        helper.connectAndQuery(DELETE_RESUME, ps -> {
            executeStatement(helper.doStatement(ps, uuid), uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.executeTransaction(conn -> {
            Map<String, Resume> map = new HashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(SELECT_RESUMES_WITH_CONTACTS)) {
                try (ResultSet res = ps.executeQuery()) {
                    while (res.next()) {
                        String uuid = res.getString("uuid").trim();
                        Resume resume = map.get(uuid);
                        if (resume == null) {
                            resume = new Resume(uuid, res.getString("full_name"));
                            map.put(uuid, resume);
                        }
                        addContact(res, resume);
                    }
                }
            }
            ArrayList<Resume> resumes = new ArrayList<>(map.values());
            resumes.sort(AbstractStorage.RESUME_COMPARATOR);
            return new ArrayList<>(resumes);
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

    private void executeStatement(PreparedStatement preparedStatement, String uuid) throws SQLException {
        if (preparedStatement.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    private void addContact(ResultSet result, Resume resume) throws SQLException {
        String contact = result.getString("value");
        if (contact != null) {
            ContactType contactType = ContactType.valueOf(result.getString("type"));
            resume.addContact(contactType, contact);
        }
    }
}