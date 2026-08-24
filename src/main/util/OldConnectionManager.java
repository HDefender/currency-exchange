package main.util;


import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class OldConnectionManager {
    public static final String URL_KEY = "db.url";
    public static final String POOL_SIZE_KEY = "db.pool.size";
    private static final int DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue< Connection> pool;
    private static List<Connection> sourceConnections;

    static {
        loadDriver();
        try {
            initConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private OldConnectionManager(){
    }

    private static void initConnectionPool() throws SQLException {
        String poolSize = PropertiesUtil.getProperty(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(Integer.parseInt(poolSize));
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++){
            Connection connection = open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(OldConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            :method.invoke(connection, args));
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }

    public static Connection get (){
        try {
            return pool.take();
        } catch (InterruptedException e) {
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
    private static Connection open() throws SQLException {
        try{
            return DriverManager.getConnection(PropertiesUtil.getProperty(URL_KEY));
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public static void closePool (){
        for (Connection sourceConnection : sourceConnections) {
            try {
                sourceConnection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
