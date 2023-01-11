package com.andrew121410.ccutils.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface ISQL {

    void connect();

    void disconnect();

    boolean isConnected();

    ResultSet getResult(String command);

    void executeCommand(String command);

    PreparedStatement executeCommandPreparedStatement(String command);

    Connection getConnection();
}