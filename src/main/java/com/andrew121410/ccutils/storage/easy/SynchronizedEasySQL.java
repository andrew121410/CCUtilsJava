package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;

public class SynchronizedEasySQL implements IEasySQL {

    private final SynchronizedMultiTableEasySQL synchronizedMultiTableEasySQL;

    private final String tableName;

    public SynchronizedEasySQL(String tableName, SynchronizedMultiTableEasySQL synchronizedMultiTableEasySQL) {
        this.tableName = tableName;
        this.synchronizedMultiTableEasySQL = synchronizedMultiTableEasySQL;
    }

    @Override
    public void create(List<String> list, boolean primaryKey) {
        synchronized (this.synchronizedMultiTableEasySQL) {
            this.synchronizedMultiTableEasySQL.create(this.tableName, list, primaryKey);
        }
    }

    @Override
    public void save(Multimap<String, SQLDataStore> multimap) throws SQLException {
        synchronized (this.synchronizedMultiTableEasySQL) {
            this.synchronizedMultiTableEasySQL.save(this.tableName, multimap);
        }
    }

    @Override
    public void save(SQLDataStore map) throws SQLException {
        synchronized (this.synchronizedMultiTableEasySQL) {
            this.synchronizedMultiTableEasySQL.save(this.tableName, map);
        }
    }

    @Override
    public Multimap<String, SQLDataStore> get(SQLDataStore toGetMap) throws SQLException {
        synchronized (this.synchronizedMultiTableEasySQL) {
            return this.synchronizedMultiTableEasySQL.get(this.tableName, toGetMap);
        }
    }

    @Override
    public Multimap<String, SQLDataStore> getEverything() throws SQLException {
        synchronized (this.synchronizedMultiTableEasySQL) {
            return this.synchronizedMultiTableEasySQL.getEverything(this.tableName);
        }
    }

    @Override
    public void delete(SQLDataStore sqlDataStore) throws SQLException {
        synchronized (this.synchronizedMultiTableEasySQL) {
            this.synchronizedMultiTableEasySQL.delete(this.tableName, sqlDataStore);
        }
    }

    @Override
    public void addColumn(String columnName, String after) {
        synchronized (this.synchronizedMultiTableEasySQL) {
            this.synchronizedMultiTableEasySQL.addColumn(this.tableName, columnName, after);
        }
    }

    @Override
    public void deleteColumn(String columnName) {
        synchronized (this.synchronizedMultiTableEasySQL) {
            this.synchronizedMultiTableEasySQL.deleteColumn(this.tableName, columnName);
        }
    }

    @Override
    public List<String> getAllTables() throws SQLException {
        synchronized (this.synchronizedMultiTableEasySQL) {
            return this.synchronizedMultiTableEasySQL.getAllTables();
        }
    }

    @Override
    public void deleteTable() throws SQLException {
        synchronized (this.synchronizedMultiTableEasySQL) {
            this.synchronizedMultiTableEasySQL.deleteTable(this.tableName);
        }
    }
}
