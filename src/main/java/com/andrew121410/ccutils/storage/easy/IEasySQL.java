package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;

public interface IEasySQL {

    void create(List<String> list, boolean primaryKey);

    void save(Multimap<String, SQLDataStore> multimap) throws SQLException;

    void save(SQLDataStore map) throws SQLException;

    Multimap<String, SQLDataStore> get(SQLDataStore toGetMap) throws SQLException;

    Multimap<String, SQLDataStore> getEverything() throws SQLException;

    void delete(SQLDataStore sqlDataStore) throws SQLException;

    void addColumn(String columnName, String after);

    void deleteColumn(String columnName);

    List<String> getAllTables() throws SQLException;
}
