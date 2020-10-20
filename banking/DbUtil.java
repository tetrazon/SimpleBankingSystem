package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static Connection dbConnection = null;

    public static Connection getConnection(String dbName) throws SQLException {
        if (dbConnection == null || dbConnection.isClosed()) {
            try {
                String dbDriver = "org.sqlite.JDBC";
                String connectionUrl = "jdbc:sqlite:" + dbName;

                    Class.forName(dbDriver);
                    dbConnection = DriverManager.getConnection(connectionUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbConnection;
    }
}
