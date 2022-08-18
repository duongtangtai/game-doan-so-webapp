package com.example.myproject1.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
@FunctionalInterface
public interface JDBCExecute <T>{
    /**
     * A functional interface to store lambda expression. In this case we handle preparedStatement
     */
    T processStatement(PreparedStatement preparedStatement) throws SQLException;
}
