package com.andrew121410.ccutils.storage.easy;

import com.andrew121410.ccutils.storage.ISQL;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MultiTableEasySQL implements IMultiTableEasySQL {

    private ISQL isql;

    public MultiTableEasySQL(ISQL isql) {
        this.isql = isql;
    }

    @Override
    public void create(String tableName, List<String> list, boolean primaryKey) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (");
        int a = 0;
        for (String s : list) {
            if (a == 0) {
//                this.primaryKey = s;
                stringBuilder.append("`").append(s).append("`").append(" TEXT");
                if (primaryKey) stringBuilder.append(" PRIMARY KEY");
            } else stringBuilder.append(",`").append(s).append("`").append(" TEXT");
            a++;
        }
        stringBuilder.append(");");
        isql.connect();
        isql.executeCommand(stringBuilder.toString());
        isql.disconnect();
    }

    private StringBuilder makeInsertCommand(String tableName, SQLDataStore sqlDataStore) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("INSERT INTO ").append(tableName).append(" (");
        int a = 0;
        for (String key : sqlDataStore.keySet()) {
            if (a == 0) {
                commandBuilder.append(key);
            } else commandBuilder.append(",").append(key);
            a++;
        }
        commandBuilder.append(") VALUES (");

        for (int i = 0; i < a; i++) {
            if (i == 0) commandBuilder.append("?");
            else commandBuilder.append(",?");
        }
        commandBuilder.append(");");
        return commandBuilder;
    }

    @Override
    public void save(String tableName, Multimap<String, SQLDataStore> multimap) throws SQLException {
        if (multimap.isEmpty()) {
            throw new IllegalArgumentException("The multimap is empty!");
        }
        String insertCommand = makeInsertCommand(tableName, multimap.values().stream().findFirst().get()).toString();

        this.isql.connect();
        PreparedStatement preparedStatement = this.isql.executeCommandPreparedStatement(insertCommand);

        this.isql.getConnection().setAutoCommit(false);
        int i = 0;
        for (SQLDataStore sqlDataStore : multimap.values()) {

            int b = 1;
            for (String value : sqlDataStore.values()) {
                preparedStatement.setString(b, value);
                b++;
            }

            preparedStatement.addBatch();

            i++;
            if (i % 1000 == 0 || i == multimap.size()) {
                preparedStatement.executeBatch();
                this.isql.getConnection().commit();
            }
        }

        this.isql.getConnection().setAutoCommit(true);
        this.isql.disconnect();
    }

    @Override
    public void save(String tableName, SQLDataStore sqlDataStore) throws SQLException {
        isql.connect();
        PreparedStatement preparedStatement = this.isql.executeCommandPreparedStatement(makeInsertCommand(tableName, sqlDataStore).toString());

        // Set the values
        int b = 1;
        for (String value : sqlDataStore.values()) {
            preparedStatement.setString(b, value);
            b++;
        }

        preparedStatement.executeUpdate();
        isql.disconnect();
    }

    private Multimap<String, SQLDataStore> read(ResultSet resultSet) throws SQLException {
        Multimap<String, SQLDataStore> multimap = ArrayListMultimap.create();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        while (resultSet.next()) {
            SQLDataStore sqlDataStore = new SQLDataStore();
            String key = null;
            for (int i = 1; i <= columns; ++i) {
                String columnName = metaData.getColumnName(i);
                String value = resultSet.getString(i);
                if (i == 1) key = value;
                sqlDataStore.put(columnName, value);
            }
            multimap.put(key, sqlDataStore);
        }
        return multimap;
    }

    @Override
    public Multimap<String, SQLDataStore> get(String tableName, SQLDataStore toGetMap) throws SQLException {
        // Make the select command
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(tableName).append(" WHERE (");
        int a = 0;
        for (String key : toGetMap.keySet()) {
            if (a == 0) {
                stringBuilder.append(key).append("=?");
            } else {
                stringBuilder.append(" AND ").append(key).append("=?");
            }
            a++;
        }
        stringBuilder.append(");");

        this.isql.connect();
        PreparedStatement preparedStatement = this.isql.executeCommandPreparedStatement(stringBuilder.toString());

        // Set the values
        int b = 1;
        for (String value : toGetMap.values()) {
            preparedStatement.setString(b, value);
            b++;
        }

        ResultSet resultSet = preparedStatement.executeQuery(); // Get the result
        Multimap<String, SQLDataStore> multimap = read(resultSet); // Read the result
        isql.disconnect();
        return multimap;
    }

    @Override
    public Multimap<String, SQLDataStore> getEverything(String tableName) throws SQLException {
        isql.connect();
        ResultSet resultSet = isql.getResult("SELECT * FROM " + tableName + ";"); // Get the result
        Multimap<String, SQLDataStore> multimap = read(resultSet); // Read the result
        isql.disconnect();
        return multimap;
    }

    @Override
    public void delete(String tableName, SQLDataStore sqlDataStore) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ").append(tableName).append(" WHERE ");
        int a = 0;
        for (String key : sqlDataStore.keySet()) {
            if (a == 0) {
                stringBuilder.append(key).append("=?");
            } else stringBuilder.append(" AND ").append(key).append("=?");
            a++;
        }

        this.isql.connect();
        PreparedStatement preparedStatement = this.isql.executeCommandPreparedStatement(stringBuilder.toString());

        // Set the values
        int b = 1;
        for (String value : sqlDataStore.values()) {
            preparedStatement.setString(b, value);
            b++;
        }

        preparedStatement.executeUpdate();
        this.isql.disconnect();
    }

    @Override
    public void addColumn(String tableName, String columnName, String after) {
        String command = "ALTER TABLE " + tableName + " ADD COLUMN `" + columnName + "` TEXT";
        if (after != null) command = command + " AFTER " + after;
        command = command + ";";
        isql.connect();
        isql.executeCommand(command);
        isql.disconnect();
    }

    @Override
    public void deleteColumn(String tableName, String columnName) {
        String command = "ALTER TABLE " + tableName + " DROP COLUMN `" + columnName + "`;";
        isql.connect();
        isql.executeCommand(command);
        isql.disconnect();
    }

    @Override
    public List<String> getAllTables() throws SQLException {
        isql.connect();
        ResultSet resultSet = isql.getResult("SHOW TABLES;");
        List<String> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        isql.disconnect();
        return list;
    }

    public ISQL getISQL() {
        return isql;
    }
}
