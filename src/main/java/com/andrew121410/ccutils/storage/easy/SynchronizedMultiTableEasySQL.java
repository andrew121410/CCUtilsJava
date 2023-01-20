package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;

public class SynchronizedMultiTableEasySQL implements IMultiTableEasySQL {

    private MultiTableEasySQL multiTableEasySQL;

    public SynchronizedMultiTableEasySQL(MultiTableEasySQL multiTableEasySQL) {
        this.multiTableEasySQL = multiTableEasySQL;
    }

    @Override
    public synchronized void create(String tableName, List<String> list, boolean primaryKey) {
        this.multiTableEasySQL.create(tableName, list, primaryKey);
    }

    @Override
    public synchronized void save(String tableName, Multimap<String, SQLDataStore> multimap) throws SQLException {
        this.multiTableEasySQL.save(tableName, multimap);
    }

    @Override
    public synchronized void save(String tableName, SQLDataStore sqlDataStore) throws SQLException {
        this.multiTableEasySQL.save(tableName, sqlDataStore);
    }

    @Override
    public synchronized Multimap<String, SQLDataStore> get(String tableName, SQLDataStore toGetMap) throws SQLException {
        return this.multiTableEasySQL.get(tableName, toGetMap);
    }

    @Override
    public synchronized Multimap<String, SQLDataStore> getEverything(String tableName) throws SQLException {
        return this.multiTableEasySQL.getEverything(tableName);
    }

    @Override
    public synchronized void delete(String tableName, SQLDataStore sqlDataStore) throws SQLException {
        this.multiTableEasySQL.delete(tableName, sqlDataStore);
    }

    @Override
    public synchronized void addColumn(String tableName, String columnName, String after) {
        this.multiTableEasySQL.addColumn(tableName, columnName, after);
    }

    @Override
    public synchronized void deleteColumn(String tableName, String columnName) {
        this.multiTableEasySQL.deleteColumn(tableName, columnName);
    }

    @Override
    public synchronized List<String> getAllTables() throws SQLException {
        return this.multiTableEasySQL.getAllTables();
    }
}
