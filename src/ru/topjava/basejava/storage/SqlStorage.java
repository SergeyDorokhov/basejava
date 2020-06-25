package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        helper.connectAndQuery(DELETE_RESUMES, ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        helper.connectAndQuery(INSERT_RESUME, ps -> {
            try {
                helper.doStatement(ps, resume.getUuid(), resume.getFullName()).execute();
            } catch (SQLException e) {
                helper.processException(resume, e);
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return helper.connectAndQuery(SELECT_RESUME, ps -> {
            try (ResultSet result = helper.doStatement(ps, uuid).executeQuery()) {
                if (result.next()) {
                    return new Resume(uuid, result.getString("full_name"));
                }
                LOG.warning("Resume " + uuid + " not exist");
                throw new NotExistStorageException(uuid);
            }
        });
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        helper.connectAndQuery(UPDATE_RESUME, ps -> {
            PreparedStatement preparedStatement = helper.doStatement(ps, resume.getFullName(), resume.getUuid());
            executeStatement(preparedStatement, resume.getUuid());
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        helper.connectAndQuery(DELETE_RESUME, ps -> {
            PreparedStatement preparedStatement = helper.doStatement(ps, uuid);
            executeStatement(preparedStatement, uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.connectAndQuery(SELECT_RESUMES, ps -> {
            List<Resume> resumes = new ArrayList<>();
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    resumes.add(new Resume(result.getString("uuid").trim()
                            , result.getString("full_name")));
                }
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return helper.connectAndQuery(COUNT_RESUMES, ps -> {
            try (ResultSet selectCount = ps.executeQuery()) {
                return selectCount.next() ? selectCount.getInt("COUNT") : 0;
            }
        });
    }

    private void executeStatement(PreparedStatement preparedStatement, String uuid) throws SQLException {
        if (preparedStatement.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }
}