package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;

public interface IMultiTableEasySQL {


    void create(String tableName, List<String> list, boolean primaryKey);

    void save(String tableName, Multimap<String, SQLDataStore> multimap) throws SQLException;

    void save(String tableName, SQLDataStore sqlDataStore) throws SQLException;

    Multimap<String, SQLDataStore> get(String tableName, SQLDataStore toGetMap) throws SQLException;

    Multimap<String, SQLDataStore> getEverything(String tableName) throws SQLException;

    void delete(String tableName, SQLDataStore sqlDataStore) throws SQLException;

    void addColumn(String tableName, String columnName, String after);

    void deleteColumn(String tableName, String columnName);

    List<String> getAllTables() throws SQLException;

    void deleteTable(String tableName) throws SQLException;
}
