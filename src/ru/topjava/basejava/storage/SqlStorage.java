package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.util.SqlHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ru.topjava.basejava.util.SqlHelper.*;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        SqlHelper.setConnectionFactory(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        connectAndQuery(connection -> {
            connection.prepareStatement(DELETE_RESUMES).execute();
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        connectAndQuery(connection -> {
            checkIsNotExist(connection, resume);
            LOG.info("Save " + resume);
            preparedStatement(connection, INSERT_RESUME, resume.getUuid(), resume.getFullName()).execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return new Resume(uuid, processResult("full_name",
                connectAndQuery(connection -> checkExist(connection, uuid))));
    }

    @Override
    public void update(Resume resume) {
        connectAndQuery(connection -> {
            checkExist(connection, resume.getUuid());
            LOG.info("Update " + resume);
            preparedStatement(connection, UPDATE_RESUME, resume.getFullName(), resume.getUuid()).execute();
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        connectAndQuery(connection -> {
            checkExist(connection, uuid);
            LOG.info("Delete " + uuid);
            preparedStatement(connection, DELETE_RESUME, uuid).execute();
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        ResultSet selectAll = connectAndQuery(connection -> connection.prepareStatement(SELECT_RESUMES).executeQuery());
        List<Resume> resumes = new ArrayList<>();
        while (isNext(selectAll)) {
            resumes.add(new Resume(processResult("uuid", selectAll).trim()
                    , processResult("full_name", selectAll)));
        }
        LOG.info("getAllSorted");
        return resumes;
    }

    @Override
    public int size() {
        ResultSet resultSet = connectAndQuery((connection -> {
            ResultSet selectCount = connection.prepareStatement(COUNT_RESUMES).executeQuery();
            isNext(selectCount);
            return selectCount;
        }));
        return Integer.parseInt(processResult("COUNT", resultSet));
    }

    private boolean checkIsNotExist(Connection connection, Resume resume) throws SQLException {
        ResultSet result = preparedStatement(connection, SELECT_RESUME, resume.getUuid()).executeQuery();
        if (result.next()) {
            LOG.warning("Resume " + resume.getUuid() + " exist");
            throw new ExistStorageException(resume.getUuid());
        }
        return true;
    }

    private ResultSet checkExist(Connection connection, String uuid) throws SQLException {
        ResultSet result = preparedStatement(connection, SELECT_RESUME, uuid).executeQuery();
        if (!result.next()) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return result;
    }
}