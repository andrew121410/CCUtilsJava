package com.andrew121410.ccutils.storage;

import java.sql.*;

public class MySQL implements ISQL {

    private final String host;
    private final String database;
    private final String username;
    private final transient String password;
    private final String port;

    private Connection connection;

    public MySQL(String host, String dataBase, String userName, String passWord, String port) {
        this.host = host;
        this.database = dataBase;
        this.username = userName;
        this.password = passWord;
        this.port = port;
    }

    @Override
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true&verifyServerCertificate=false&useSSL=true&serverTimezone=EST";
        try {
            this.connection = DriverManager.getConnection(url, this.username, this.password);
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
