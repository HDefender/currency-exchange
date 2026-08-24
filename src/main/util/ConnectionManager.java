package main.util;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;



public class ConnectionManager {
    public static final String URL_KEY = "db.url";

    static {
        loadDriver();
    }

    private ConnectionManager(){
    }

    public static DataSource getDataSource() throws SQLException {
        var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(PropertiesUtil.getProperty(URL_KEY));
        return dataSource;
    }

    public static Connection get (){
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQL is not found",e);
        }
    }
}