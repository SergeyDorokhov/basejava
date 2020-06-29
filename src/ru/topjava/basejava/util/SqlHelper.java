package ru.topjava.basejava.util;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.ContactType;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public interface Query<T> {
        T execute(PreparedStatement preparedStatement) throws SQLException;
    }

    public interface SqlTransaction<T> {
        T execute(Connection conn) throws SQLException;
    }

    public <T> T connectAndQuery(String sql, Query<T> query) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            return query.execute(ps);
        } catch (SQLException e) {
            throw processException(e);
        }
    }

    public <T> T executeTransaction(SqlTransaction<T> executor) {
        try {
            try (Connection connection = connectionFactory.getConnection()) {
                try {
                    connection.setAutoCommit(false);
                    T result = executor.execute(connection);
                    connection.commit();
                    return result;
                } catch (SQLException e) {
                    connection.rollback();
                    throw processException(e);
                }
            }
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

    public void executeStatement(PreparedStatement preparedStatement, String uuid) throws SQLException {
        if (preparedStatement.executeUpdate() == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    public void addContact(ResultSet result, Resume resume) throws SQLException {
        String contact = result.getString("value");
        if (contact != null) {
            ContactType contactType = ContactType.valueOf(result.getString("type"));
            resume.addContact(contactType, contact);
        }
    }

    public void saveContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid)" +
                "VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                doStatement(ps, entry.getValue(), entry.getKey().name(), resume.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void deleteContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
            doStatement(ps, resume.getUuid()).execute();
        }
    }

    public StorageException processException(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            throw new ExistStorageException(null);
        }
        throw new StorageException(e);
    }
}
