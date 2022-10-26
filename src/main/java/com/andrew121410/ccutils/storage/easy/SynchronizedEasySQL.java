package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SynchronizedEasySQL implements IEasySQL {

    private final EasySQL easySQL;

    public SynchronizedEasySQL(EasySQL easySQL) {
        this.easySQL = easySQL;
    }

    @Override
    public synchronized void create(List<String> list, boolean primaryKey) {
        this.easySQL.create(list, primaryKey);
    }

    @Override
    public synchronized void save(Multimap<String, SQLDataStore> multimap) throws SQLException {
        this.easySQL.save(multimap);
    }

    @Override
    public synchronized void save(Map<String, String> map) throws SQLException {
        this.easySQL.save(map);
    }

    @Override
    public synchronized Multimap<String, SQLDataStore> get(Map<String, String> fromMap) {
        return this.easySQL.get(fromMap);
    }

    @Override
    public synchronized Multimap<String, SQLDataStore> getEverything() throws SQLException {
        return this.easySQL.getEverything();
    }

    @Override
    public synchronized void delete(Map<String, String> map) {
        this.easySQL.delete(map);
    }

    @Override
    public synchronized void addColumn(String columnName, String after) {
        this.easySQL.addColumn(columnName, after);
    }

    @Override
    public synchronized void deleteColumn(String columnName) {
        this.easySQL.deleteColumn(columnName);
    }
}
