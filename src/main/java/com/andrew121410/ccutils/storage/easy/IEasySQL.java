package com.andrew121410.ccutils.storage.easy;

import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IEasySQL {

    void create(List<String> list, boolean primaryKey);

    void save(Multimap<String, SQLDataStore> multimap) throws SQLException;

    void save(Map<String, String> map) throws SQLException;

    Multimap<String, SQLDataStore> get(Map<String, String> fromMap);

    Multimap<String, SQLDataStore> getEverything() throws SQLException;

    void delete(Map<String, String> map);

    void addColumn(String columnName, String after);

    void deleteColumn(String columnName);
}
