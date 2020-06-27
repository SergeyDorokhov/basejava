package ru.topjava.basejava.sql;

import java.sql.Connection;

public interface SqlTransaction<T> {
    T execute(Connection connection);
}
