package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;

public class EasySQL implements IEasySQL {

    private final MultiTableEasySQL multiTableEasySQL;

    private final String tableName;

    public EasySQL(String tableName, MultiTableEasySQL multiTableEasySQL) {
        this.tableName = tableName;
        this.multiTableEasySQL = multiTableEasySQL;
    }

    @Override
    public void create(List<String> list, boolean primaryKey) {
        this.multiTableEasySQL.create(this.tableName, list, primaryKey);
    }

    @Override
    public void save(Multimap<String, SQLDataStore> multimap) throws SQLException {
        this.multiTableEasySQL.save(this.tableName, multimap);
    }

    @Override
    public void save(SQLDataStore map) throws SQLException {
        this.multiTableEasySQL.save(this.tableName, map);
    }

    @Override
    public Multimap<String, SQLDataStore> get(SQLDataStore toGetMap) throws SQLException {
        return this.multiTableEasySQL.get(this.tableName, toGetMap);
    }

    @Override
    public Multimap<String, SQLDataStore> getEverything() throws SQLException {
        return this.multiTableEasySQL.getEverything(this.tableName);
    }

    @Override
    public void delete(SQLDataStore sqlDataStore) throws SQLException {
        this.multiTableEasySQL.delete(this.tableName, sqlDataStore);
    }

    @Override
    public void addColumn(String columnName, String after) {
        this.multiTableEasySQL.addColumn(this.tableName, columnName, after);
    }

    @Override
    public void deleteColumn(String columnName) {
        this.multiTableEasySQL.deleteColumn(this.tableName, columnName);
    }

    @Override
    public List<String> getAllTables() throws SQLException {
        return this.multiTableEasySQL.getAllTables();
    }
}
