package com.andrew121410.CCUtils.storage.easy;

import java.util.LinkedHashMap;
import java.util.Map;

public class SQLDataStore {

    private Map<String, String> map;

    public SQLDataStore() {
        this.map = new LinkedHashMap<>();
    }

    public Map<String, String> getMap() {
        return map;
    }
}
