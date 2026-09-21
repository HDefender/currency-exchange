package util;

import com.zaxxer.hikari.HikariDataSource;
import exception.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;



public class ConnectionManager {
    public static final String URL_KEY = "db.url";

    private static final HikariDataSource DATA_SOURCE;

    static {
        loadDriver();
        try {
            DATA_SOURCE = createDataSource();
        } catch (SQLException e) {
            throw new DatabaseException("Failure to create connection pool");
        }
    }

    private ConnectionManager(){
    }

    public static HikariDataSource createDataSource() throws SQLException {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(PropertiesUtil.getProperty(URL_KEY));
        return dataSource;
    }

    public static Connection get (){
        try {
            return DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Failure to get connection");
        }
    }

    public static void close() {
        DATA_SOURCE.close();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Driver SQL is not found");
        }
    }
}