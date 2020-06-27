package ru.topjava.basejava.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Query<T> {
    T execute(PreparedStatement preparedStatement) throws SQLException;
}
