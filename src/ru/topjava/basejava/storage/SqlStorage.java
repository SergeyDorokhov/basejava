package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private final SqlHelper helper;
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private static final String SELECT_RESUME = "SELECT * FROM resume WHERE uuid=?";
    private static final String DELETE_RESUMES = "DELETE FROM resume";
    private static final String INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
    private static final String UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid=?";
    private static final String DELETE_RESUME = "DELETE FROM resume WHERE uuid =?";
    private static final String SELECT_RESUMES = "SELECT * FROM resume ORDER BY full_name, uuid";
    private static final String COUNT_RESUMES = "SELECT COUNT(*) from resume";

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        helper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        helper.connectAndQuery(conn -> conn.prepareStatement(DELETE_RESUMES).execute());
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        helper.connectAndQuery(conn -> {
            ResultSet result = helper.doStatement(conn, SELECT_RESUME, resume.getUuid()).executeQuery();
            if (result.next()) {
                LOG.warning("Resume " + resume.getUuid() + " exist");
                throw new ExistStorageException(resume.getUuid());
            }
            return helper.doStatement(conn, INSERT_RESUME, resume.getUuid(), resume.getFullName()).execute();
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return helper.connectAndQuery(conn -> {
            ResultSet result = helper.doStatement(conn, SELECT_RESUME, uuid).executeQuery();
            if (result.next()) {
                return new Resume(uuid, result.getString("full_name"));
            }
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        });
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        helper.connectAndQuery(conn -> {
            if (helper.doStatement(conn, UPDATE_RESUME, resume.getFullName()
                    , resume.getUuid()).executeUpdate() == 0) {
                throw new NotExistStorageException(resume.getUuid());
            }
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        helper.connectAndQuery(conn -> {
            if (helper.doStatement(conn, DELETE_RESUME, uuid).executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.connectAndQuery(conn -> {
            ResultSet result = conn.prepareStatement(SELECT_RESUMES).executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (result.next()) {
                resumes.add(new Resume(result.getString("uuid").trim()
                        , result.getString("full_name")));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return helper.connectAndQuery((conn -> {
            ResultSet selectCount = conn.prepareStatement(COUNT_RESUMES).executeQuery();
            return selectCount.next() ? selectCount.getInt("COUNT") : 0;
        }));
    }
}