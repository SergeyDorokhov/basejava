package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private final SqlHelper helper;
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private static final String INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
    private static final String SELECT_RESUME = "SELECT * FROM resume r LEFT JOIN contact c " +
            "ON r.uuid = c.resume_uuid WHERE r.uuid =?";
    private static final String DELETE_RESUME = "DELETE FROM resume WHERE uuid =?";
    private static final String DELETE_RESUMES = "DELETE FROM resume";
    private static final String UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid=?";
    private static final String COUNT_RESUMES = "SELECT COUNT(*) from resume";
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
            helper.saveContacts(conn, resume);
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
                    helper.addContact(result, resume);
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
                helper.executeStatement(ps, resume.getUuid());
            }
            helper.deleteContacts(conn, resume);
            helper.saveContacts(conn, resume);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        helper.connectAndQuery(DELETE_RESUME, ps -> {
            helper.executeStatement(helper.doStatement(ps, uuid), uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.executeTransaction(conn -> {
            Map<String, Resume> map = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(SELECT_RESUMES_WITH_CONTACTS)) {
                try (ResultSet res = ps.executeQuery()) {
                    while (res.next()) {
                        String uuid = res.getString("uuid").trim();
                        String name = res.getString("full_name");
                        map.computeIfAbsent(uuid, k -> new Resume(uuid, name));
                        helper.addContact(res, map.get(uuid));
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
}