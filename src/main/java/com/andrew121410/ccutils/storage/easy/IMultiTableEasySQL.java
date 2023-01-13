package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IMultiTableEasySQL {


    public void create(String tableName, List<String> list, boolean primaryKey);

    public void save(String tableName, Multimap<String, SQLDataStore> multimap) throws SQLException;

    public void save(String tableName, SQLDataStore sqlDataStore) throws SQLException;

    public Multimap<String, SQLDataStore> get(String tableName, SQLDataStore toGetMap) throws SQLException;

    public Multimap<String, SQLDataStore> getEverything(String tableName) throws SQLException;

    public void delete(String tableName, Map<String, String> map);

    public void addColumn(String tableName, String columnName, String after);

    public void deleteColumn(String tableName, String columnName);

    List<String> getAllTables() throws SQLException;
}
