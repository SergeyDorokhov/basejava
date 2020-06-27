package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.ContactType;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        helper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(INSERT_RESUME)) {
                helper.doStatement(ps, resume.getUuid(), resume.getFullName()).execute();
            } catch (SQLException e) {
                helper.processException(resume.getUuid(), e);
            }
            try (PreparedStatement ps = conn.prepareStatement(INSERT_CONTACT)) {
                for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                    helper.doStatement(ps, entry.getKey().name(), entry.getValue(), resume.getUuid());
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLException e) {
                throw new StorageException(e);
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
                    final ContactType contactType = ContactType.valueOf(result.getString("type"));
                    final String contact = result.getString("value");
                    resume.addContact(contactType, contact);
                } while (result.next());
                return resume;
            }
        });
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        helper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(UPDATE_RESUME)) {
                PreparedStatement finalStatement = helper.doStatement(ps, resume.getFullName(), resume.getUuid());
                executeStatement(finalStatement, resume.getUuid());
            } catch (SQLException e) {
                throw new StorageException(e);
            }
            try (PreparedStatement ps = conn.prepareStatement(UPDATE_CONTACTS)) {
                for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                    PreparedStatement prep = helper.doStatement(ps, entry.getValue(), entry.getKey().name(), resume.getUuid());
                    prep.addBatch();
                }
                ps.executeBatch();
            } catch (SQLException e) {
                throw new StorageException(e);
            }
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
        List<Resume> resumes = helper.connectAndQuery(SELECT_RESUMES, ps -> {
            List<Resume> list = new ArrayList<>();
            try (ResultSet result = ps.executeQuery()) {
                while (result.next()) {
                    list.add(new Resume(result.getString("uuid").trim()
                            , result.getString("full_name")));
                }
            }
            return list;
        });
        for (Resume resume : resumes) {
            helper.connectAndQuery(SELECT_CONTACT, ps -> {
                helper.doStatement(ps, resume.getUuid());
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        resume.addContact(ContactType.valueOf(resultSet.getString("type")),
                                resultSet.getString("value"));
                    }
                }
                return null;
            });
        }
        return resumes;
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