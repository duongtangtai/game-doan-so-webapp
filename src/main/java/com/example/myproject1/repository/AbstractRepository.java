package com.example.myproject1.repository;

import com.example.myproject1.jdbc.JDBCExecute;
import com.example.myproject1.jdbc.MySQLConnection;
import com.example.myproject1.exception.DatabaseNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractRepository {
    /**
     * Process a SQL query with Autocloseable connection. This is a wrapper lambda due to handling exceptions.
     * @return an Object
     */
    protected <T> T processObjQuery(String sql, JDBCExecute<T> processor) {
        try (Connection connection = MySQLConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            return processor.processStatement(preparedStatement);
        } catch (SQLException e) {
            System.out.println("asd");
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }
    /**
     * Process a SQL query with Autocloseable connection. This is a wrapper lambda due to handling exceptions.
     * @return a List of Objects
     */
    protected <T> List<T> processListQuery(String sql, JDBCExecute<List<T>> processor) {
        try (Connection connection = MySQLConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            return processor.processStatement(preparedStatement);
        } catch (SQLException e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }

    /**
     * The method fills the preparedStatement with varargs
     * @param preparedStatement a preparedStatement needs to be filled
     * @param varList list of objects to fill in
     */
    @SafeVarargs
    protected final <T> PreparedStatement fillStatement(PreparedStatement preparedStatement, T... varList) throws SQLException {
        int start = 1;
        for (T e : varList) {
            preparedStatement.setObject(start, e);
            start++;
        }
        return preparedStatement;
    }
}
