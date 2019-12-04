package CCUtils.Storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface ISQL {

    public void Connect();

    public void Disconnect();

    public boolean isConnected();

    public ResultSet GetResult(String command);

    public ResultSet GetResultPreparedStatement(String command);

    public void ExecuteCommand(String command);

    public PreparedStatement ExecuteCommandPreparedStatement(String command);

}