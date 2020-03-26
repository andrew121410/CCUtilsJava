package CCUtils.Storage.easy;

import CCUtils.Storage.ISQL;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EasySQL {

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
        isql.Connect();
        isql.ExecuteCommand(stringBuilder.toString());
        isql.Disconnect();
    }

    public void save(Map<String, String> map) throws SQLException {
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

        isql.Connect();
        PreparedStatement preparedStatement = this.isql.ExecuteCommandPreparedStatement(commandBuilder.toString());
        int b = 1;
        for (Map.Entry<String, String> stringObjectEntry : map.entrySet()) {
            String key = stringObjectEntry.getKey();
            String value = stringObjectEntry.getValue();

            preparedStatement.setString(b, value);
            b++;
        }
        preparedStatement.executeUpdate();
        isql.Disconnect();
    }

    public MultiValuedMap<String, String> get(Map<String, String> fromMap) {
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM ").append(tableName).append(" WHERE (");
        int a = 0;
        for (Map.Entry<String, String> stringStringEntry : fromMap.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();

            if (a == 0) {
                stringBuilder.append(key).append("='").append(value).append("'");
            } else stringBuilder.append(" AND ").append(key).append("='").append(value).append("'");

            a++;
        }
        stringBuilder.append(");");

        isql.Connect();
        ResultSet rs = isql.GetResult(stringBuilder.toString());
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; ++i) {
                    String key1 = md.getColumnName(i);
                    String value = rs.getString(i);
                    map.put(key1, value);
                }
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            isql.Disconnect();
        }
        return null;
    }

    public MultiValuedMap<String, String> getEverything() throws SQLException {
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        isql.Connect();
        ResultSet rs = isql.GetResult("SELECT FROM * " + tableName + ";");
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columns; ++i) {
                String key1 = md.getColumnName(i);
                String value = rs.getString(i);
                map.put(key1, value);
            }
        }

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

        isql.Connect();
        isql.ExecuteCommand(stringBuilder.toString());
        isql.Disconnect();
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
