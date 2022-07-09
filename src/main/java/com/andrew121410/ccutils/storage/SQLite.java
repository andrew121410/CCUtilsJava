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

    public void disconnect() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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

    public ResultSet getResultPreparedStatement(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }
            PreparedStatement preparedStatement = this.connection.prepareStatement(command);
            preparedStatement.executeQuery();
            return preparedStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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
}
