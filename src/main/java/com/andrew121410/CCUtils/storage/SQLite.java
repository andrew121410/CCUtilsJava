package com.andrew121410.CCUtils.storage;

import java.io.File;
import java.nio.file.Path;
import java.sql.*;

public class SQLite implements ISQL {

    private String dbName;
    private File file = null;
    private Path path = null;

    private String url;

    private Connection connection;

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
            } else {
                System.out.println("[SQLITE] The connection is already disconnected!");
            }
        } catch (SQLException e3) {
            System.out.println("[SQLITE] Error 03");
            System.out.println("[SQLITE] There was an error while disconnecting!");
            e3.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e2) {
            System.out.println("[SQLITE] Error 02");
            System.out.println("[SQLITE] An error occurred while connecting!");
            e2.printStackTrace();
        }
        return false;
    }

    public ResultSet getResult(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }
            Statement st = this.connection.createStatement();
            return st.executeQuery(command);
        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out.println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }

    public ResultSet getResultPreparedStatement(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }
            PreparedStatement pst = this.connection.prepareStatement(command);
            pst.executeQuery();
            return pst.getResultSet();
        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out.println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }

    public void executeCommand(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }
            Statement st = this.connection.createStatement();
            st.executeUpdate(command);
        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out.println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }

    }

    public PreparedStatement executeCommandPreparedStatement(String command) {
        try {
            if (this.connection.isClosed()) {
                this.connect();
            }
            return this.connection.prepareStatement(command);
        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out.println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }
}
