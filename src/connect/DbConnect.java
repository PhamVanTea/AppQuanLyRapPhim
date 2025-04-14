package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CinemaManagementDB;encrypt=true;trustServerCertificate=true";
    private static final String USERNAME = "sa"; 
    private static final String PASSWORD = "sapassword"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy driver JDBC của SQL Server.", e);
        }
    }


    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}