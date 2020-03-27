package com.andrew121410.CCUtils.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface ISQL {

    void connect();

    void disconnect();

    boolean isConnected();

    ResultSet getResult(String command);

    ResultSet getResultPreparedStatement(String command);

    void executeCommand(String command);

    PreparedStatement executeCommandPreparedStatement(String command);

}