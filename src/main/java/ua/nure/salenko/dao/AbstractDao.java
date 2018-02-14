package ua.nure.salenko.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public abstract class AbstractDao {

    Connection jdbcConnection;

    private static final Logger LOGGER = Logger.getLogger(AbstractDao.class);

    private static final String driver;
    private static final String url;
    private static final String user;
    private static final String password;

    static {
        Properties prop = new Properties();
        InputStream inputStream = AbstractDao.class.getClassLoader().getResourceAsStream("/config.properties");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("AbstractDao#initialize_connection_args?IOException", e);
        }
        driver = prop.getProperty("driver");
        url = prop.getProperty("url");
        user = prop.getProperty("user");
        password = prop.getProperty("password");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOGGER.error("AbstractDao#driver?ClassNotFoundException", e);
        }
    }

    public AbstractDao() {
        jdbcConnection = getConnection();
    }

    void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = getConnection();
        }
    }

    void commit() throws SQLException {
        jdbcConnection.commit();
    }

    void rollback() throws SQLException {
        jdbcConnection.rollback();
    }

    void setAutoCommitOff() throws SQLException {
        jdbcConnection.setAutoCommit(false);
    }

    void setAutoCommitOn() throws SQLException {
        jdbcConnection.setAutoCommit(true);
    }

    void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LOGGER.error("AbstractDao#getConnection?SQLException", e);
        }
        return connection;
    }

}
