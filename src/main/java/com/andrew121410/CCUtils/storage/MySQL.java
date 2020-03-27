package com.andrew121410.CCUtils.storage;

import java.sql.*;

public class MySQL implements ISQL {

    private String Host;
    private String Database;
    private String Username;
    private String Password;
    private String Port;

    private Connection connection;

    public MySQL(String host, String dataBase, String userName, String passWord, String port) {
        this.Host = host;
        this.Database = dataBase;
        this.Username = userName;
        this.Password = passWord;
        this.Port = port;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final String url = "jdbc:mysql://" + this.Host + ":" + this.Port + "/" + this.Database + "?autoReconnect=true&verifyServerCertificate=false&useSSL=true&serverTimezone=EST";
        try {
            this.connection = DriverManager.getConnection(url, this.Username, this.Password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
            } else {
                System.out.println("[MySQL] The connection is already disconnected!");
            }
        } catch (SQLException e3) {
            System.out.println("[MySQL] Error 03");
            System.out.println("[MySQL] There was an error while disconnecting!");
            e3.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e2) {
            System.out.println("[MySQL] Error 02");
            System.out.println("[MySQL] An error occurred while connecting!");
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
            st.executeQuery(command);
            return st.getResultSet();
        } catch (SQLException e4) {
            System.out.println("[MySQL] Error 04");
            System.out.println("[MySQL] An error occurred while executing the command!");
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
            System.out.println("[MySQL] Error 04");
            System.out.println("[MySQL] An error occurred while executing the command!");
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
            System.out.println("[MySQL] Error 04");
            System.out.println("[MySQL] An error occurred while executing the command!");
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
            System.out.println("[MySQLAPI] Error 04");
            System.out.println("[MySQLAPI] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }
}
