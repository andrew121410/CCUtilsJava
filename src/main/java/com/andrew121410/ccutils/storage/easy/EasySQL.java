package com.andrew121410.ccutils.storage.easy;

import com.andrew121410.ccutils.storage.ISQL;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EasySQL implements IEasySQL {

    private ISQL isql;

    private String tableName;
    private String primaryKey;

    public EasySQL(ISQL isql, String tableName) {
        this.isql = isql;
        this.tableName = tableName;
    }

    public void create(List<String> list, boolean primaryKey) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (");
        int a = 0;
        for (String s : list) {
            if (a == 0) {
                this.primaryKey = s;
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

    private StringBuilder makeInsertCommand(Map<String, String> map) {
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append("INSERT INTO ").append(tableName).append(" (");
        int a = 0;
        for (Map.Entry<String, String> stringObjectEntry : map.entrySet()) {
            String key = stringObjectEntry.getKey();
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

    public void save(Multimap<String, SQLDataStore> multimap) throws SQLException {
        if (multimap.isEmpty()) {
            throw new IllegalArgumentException("The multimap is empty!");
        }
        String insertCommand = makeInsertCommand(multimap.values().stream().findFirst().get()).toString();

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

    public void save(Map<String, String> map) throws SQLException {
        isql.connect();
        PreparedStatement preparedStatement = this.isql.executeCommandPreparedStatement(makeInsertCommand(map).toString());

        // Set the values
        int b = 1;
        for (String value : map.values()) {
            preparedStatement.setString(b, value);
            b++;
        }

        preparedStatement.executeUpdate();
        isql.disconnect();
    }

    public Multimap<String, SQLDataStore> get(Map<String, String> fromMap) {
        Multimap<String, SQLDataStore> map = ArrayListMultimap.create();

        // Make the select command
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(tableName).append(" WHERE (");
        int a = 0;
        for (Map.Entry<String, String> entry : fromMap.entrySet()) {
            String key = entry.getKey();
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
        for (String value : fromMap.values()) {
            try {
                preparedStatement.setString(b, value);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            b++;
        }

        try {
            ResultSet rs = preparedStatement.executeQuery(); // Get the result
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            while (rs.next()) {
                SQLDataStore sqlDataStore = new SQLDataStore();
                String key = null;
                for (int i = 1; i <= columns; ++i) {
                    String columnName = metaData.getColumnName(i);
                    String value = rs.getString(i);
                    if (i == 1) key = value;
                    sqlDataStore.put(columnName, value);
                }
                map.put(key, sqlDataStore);
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            isql.disconnect();
        }
        return null;
    }

    public Multimap<String, SQLDataStore> getEverything() throws SQLException {
        Multimap<String, SQLDataStore> map = ArrayListMultimap.create();
        isql.connect();
        ResultSet rs = isql.getResult("SELECT * FROM " + tableName + ";");
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        while (rs.next()) {
            SQLDataStore sqlDataStore = new SQLDataStore();
            String realKey = null;
            for (int i = 1; i <= columns; ++i) {
                String key = md.getColumnName(i);
                String value = rs.getString(i);
                if (i == 1) realKey = value;
                sqlDataStore.putIfAbsent(key, value);
            }
            map.put(realKey, sqlDataStore);
        }
        isql.disconnect();
        return map;
    }

    public void delete(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ").append(this.tableName).append(" WHERE ");
        int a = 0;
        for (Map.Entry<String, String> maper : map.entrySet()) {
            String key = maper.getKey();
            String value = maper.getValue();
            if (a == 0) {
                stringBuilder.append(key).append("='").append(value).append("'");
            } else stringBuilder.append(" AND ").append(key).append("='").append(value).append("'");
            a++;
        }
        isql.connect();
        isql.executeCommand(stringBuilder.toString());
        isql.disconnect();
    }

    public void addColumn(String columnName, String after) {
        String command = "ALTER TABLE " + this.tableName + " ADD COLUMN `" + columnName + "` TEXT";
        if (after != null) command = command + " AFTER " + after;
        command = command + ";";
        isql.connect();
        isql.executeCommand(command);
        isql.disconnect();
    }

    public void deleteColumn(String columnName) {
        String command = "ALTER TABLE " + this.tableName + " DROP COLUMN `" + columnName + "`;";
        isql.connect();
        isql.executeCommand(command);
        isql.disconnect();
    }

    public ISQL getISQL() {
        return isql;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }
}
