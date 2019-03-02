package CCUtils.Storage;

import java.sql.*;

public class MySQL {

    private String Host;
    private String Database;
    private String Username;
    private String Password;
    private String Port;

    private Connection connection;

    public MySQL(String host, String dataBase, String userName, String passWord, String port) {
        this.Host = host;
        this.Database = dataBase;
        this.Username = userName;
        this.Password = passWord;
        this.Port = port;
    }

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final String url =
                "jdbc:mysql://" + this.Host + ":" + this.Port + "/" + this.Database + "?autoReconnect=true&verifyServerCertificate=false&useSSL=true";
// FIX       final String url = "jdbc:mysql://" + this.Host + "/" + this.Database + "?user=" + this.Username + "&password=" + this.Password;
        try {
            this.connection = DriverManager.getConnection(url, this.Username, this.Password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
                System.out.println(
                        "[MySQLAPI] The connection to the MySQL server was successfully disconnected!");
            } else {
                System.out.println("[MySQLAPI] The connection is already disconnected!");
            }
        } catch (SQLException e3) {
            System.out.println("[MySQLAPI] Error 03");
            System.out.println("[MySQLAPI] There was an error while disconnecting!");
            e3.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return !this.connection.isClosed();
        } catch (SQLException e2) {
            System.out.println("[MySQLAPI] Error 02");
            System.out.println("[MySQLAPI] An error occurred while connecting!");
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
            st.executeQuery(command);
            ResultSet rs = st.getResultSet();
            return rs;

        } catch (SQLException e4) {
            System.out.println("[MySQLAPI] Error 04");
            System.out
                    .println("[MySQLAPI] An error occurred while executing the command!");
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
            System.out.println("[MySQLAPI] Error 04");
            System.out
                    .println("[MySQLAPI] An error occurred while executing the command!");
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
            System.out.println("[MySQLAPI] Error 04");
            System.out
                    .println("[MySQLAPI] An error occurred while executing the command!");
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
            System.out.println("[MySQLAPI] Error 04");
            System.out
                    .println("[MySQLAPI] An error occurred while executing the command!");
            e4.printStackTrace();
        }
        return null;
    }
}
