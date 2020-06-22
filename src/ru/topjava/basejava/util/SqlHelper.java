package ru.topjava.basejava.util;

import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlHelper {
    private static ConnectionFactory connectionFactory;
    public static final String SELECT_RESUME = "SELECT * FROM resume WHERE uuid=?";
    public static final String DELETE_RESUMES = "DELETE FROM resume";
    public static final String INSERT_RESUME = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
    public static final String UPDATE_RESUME = "UPDATE resume SET full_name = ? WHERE uuid=?";
    public static final String DELETE_RESUME = "DELETE FROM resume WHERE uuid =?";
    public static final String SELECT_RESUMES = "SELECT * FROM resume ORDER BY full_name, uuid";
    public static final String COUNT_RESUMES = "SELECT COUNT(*) from resume";

    public static void setConnectionFactory(ConnectionFactory connectionFactory) {
        SqlHelper.connectionFactory = connectionFactory;
    }

    public interface Query {
        ResultSet execute(Connection connection) throws SQLException;
    }

    public static ResultSet connectAndQuery(Query query) {
        try (Connection connection = connectionFactory.getConnection()) {
            return query.execute(connection);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public static String processResult(String columnLabel, ResultSet resultSet) {
        try {
            return resultSet.getString(columnLabel);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public static boolean isNext(ResultSet resultSet) {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public static PreparedStatement preparedStatement(Connection connection,
                                                      String query, String... nameOfIndex) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < nameOfIndex.length; i++) {
            preparedStatement.setString(i + 1, nameOfIndex[i]);
        }
        return preparedStatement;
    }
}
