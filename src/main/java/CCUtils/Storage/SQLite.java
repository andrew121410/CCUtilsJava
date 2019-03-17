package CCUtils.Storage;

import java.io.File;
import java.sql.*;

public class SQLite implements ISQL {

    private String dbName;
    private File file = null;

    String url;

    private Connection connection;

    public SQLite(String dbName) {
        this.dbName = dbName;
    }

    public SQLite(File file, String dbName) {
        this.file = file;
        this.dbName = dbName;
    }

    public void Connect() {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (this.file == null) {
            url = "jdbc:sqlite:" + dbName + ".db";
        } else {
            url = "jdbc:sqlite:" + this.file.getAbsolutePath() + "/" + this.dbName + ".db";
        }

        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
                System.out.println(
                        "[SQLITE] The connection to the MySQL server was successfully disconnected!");
            } else {
                System.out.println("[SQLITE] The connection is already disconnected!");
            }
        } catch (SQLException e3) {
            System.out.println("[SQLITE] Error 03");
            System.out.println("[SQLITE] There was an error while disconnecting!");
            e3.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e2) {
            System.out.println("[SQLITE] Error 02");
            System.out.println("[SQLITE] An error occurred while connecting!");
            e2.printStackTrace();
        }
        return false;
    }

    public ResultSet GetResult(String command) {
        try {
            if (this.connection.isClosed()) {
                this.Connect();
            }

            Statement st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(command);
            return rs;

        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out
                    .println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }

    public ResultSet GetResultPreparedStatement(String command) {
        try {
            if (this.connection.isClosed()) {
                this.Connect();
            }
            PreparedStatement pst = this.connection.prepareStatement(command);
            pst.executeQuery();
            ResultSet rs = pst.getResultSet();
            return rs;

        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out
                    .println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }

    public void ExecuteCommand(String command) {
        try {
            if (this.connection.isClosed()) {
                this.Connect();
            }
            Statement st = this.connection.createStatement();
            st.executeUpdate(command);
        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out
                    .println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }

    }

    public PreparedStatement ExecuteCommandPreparedStatement(String command) {
        try {
            if (this.connection.isClosed()) {
                this.Connect();
            }
            PreparedStatement pst = this.connection.prepareStatement(command);
            return pst;
        } catch (SQLException e4) {
            System.out.println("[SQLITE] Error 04");
            System.out
                    .println("[SQLITE] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }
}
