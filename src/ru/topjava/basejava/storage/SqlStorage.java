package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private static ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement deleteAllResumes = connection.prepareStatement("DELETE FROM resume");
            deleteAllResumes.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume resume) {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement selectResume = connection.prepareStatement("SELECT * FROM resume WHERE uuid=?");
            selectResume.setString(1, resume.getUuid());
            ResultSet resultSet = selectResume.executeQuery();
            if (resultSet.next()) {
                throw new ExistStorageException(resume.getUuid());
            }
            PreparedStatement insert =
                    connection.prepareStatement("insert into resume (uuid, full_name) values (?,?)");
            insert.setString(1, resume.getUuid());
            insert.setString(2, resume.getFullName());
            insert.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Resume get(String uuid) {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement selectResume = connection.prepareStatement("SELECT * FROM resume WHERE uuid=?");
            selectResume.setString(1, uuid);
            ResultSet resultSet = selectResume.executeQuery();
            if (!resultSet.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, resultSet.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume resume) {
        get(resume.getUuid());
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement updateResume
                    = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid=?");
            updateResume.setString(1, resume.getFullName());
            updateResume.setString(2, resume.getUuid());
            updateResume.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        get(uuid);
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement deleteResume
                    = connection.prepareStatement("DELETE FROM resume WHERE uuid =?");
            deleteResume.setString(1, uuid);
            deleteResume.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement selectAllResumes
                    = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid");
            ResultSet resultSet = selectAllResumes.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (resultSet.next()) {
                resumes.add(new Resume(resultSet.getString(1).trim()
                        , resultSet.getString(2)));
            }
            return resumes;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement selectSize = connection.prepareStatement("SELECT COUNT(*) from resume");
            ResultSet resultSet = selectSize.executeQuery();
            if (!resultSet.next()) {
                throw new StorageException("Query do not return rows");
            }
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}