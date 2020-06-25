package ru.topjava.basejava.util;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public interface Query<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public <T> T connectAndQuery(String sql, Query<T> query) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            return query.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public PreparedStatement doStatement(PreparedStatement ps, String... nameOfIndex) throws SQLException {
        for (int i = 0; i < nameOfIndex.length; i++) {
            ps.setString(i + 1, nameOfIndex[i]);
        }
        return ps;
    }

    public void processException(Resume resume, SQLException e) {
        if (e.getSQLState().equals("23505")) {
            throw new ExistStorageException(resume.getUuid());
        }
        throw new StorageException(e);
    }
}
