package CCUtils.Storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface ISQL {

    void Connect();

    void Disconnect();

    boolean isConnected();

    ResultSet GetResult(String command);

    ResultSet GetResultPreparedStatement(String command);

    void ExecuteCommand(String command);

    PreparedStatement ExecuteCommandPreparedStatement(String command);

}