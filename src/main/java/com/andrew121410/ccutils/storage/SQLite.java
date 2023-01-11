package com.andrew121410.ccutils.storage;

import java.io.File;
import java.nio.file.Path;
import java.sql.*;

public class SQLite implements ISQL {

    private Connection connection;

    private final String dbName;
    private File file = null;
    private Path path = null;

    public SQLite(String dbName) {
        this.dbName = dbName;
    }

    public SQLite(File file, String dbName) {
        this.file = file;
        this.dbName = dbName;
    }

    public SQLite(Path path, String dbName) {
        this.path = path;
        this.dbName = dbName;
    }

    @Override
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url;
        if (this.file == null && this.path == null) {
            url = "jdbc:sqlite:" + dbName + ".db";
        } else if (this.file != null && this.path == null) {
            url = "jdbc:sqlite:" + this.file.getAbsolutePath() + "/" + this.dbName + ".db";
        } else {
            url = "jdbc:sqlite:" + this.path.toString() + "/" + this.dbName + ".db";
        }

        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ResultSet getResult(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }

            Statement statement = this.connection.createStatement();
            return statement.executeQuery(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void executeCommand(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public PreparedStatement executeCommandPreparedStatement(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }
            return this.connection.prepareStatement(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
