package iShop;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class DBConnection {
    private static Connection connection;
    private static Date lastConnectionRequestedAt = null;

    /*
     * Singleton design pattern is implemented to avoid creation of multiple instances of DBConnetion
     */
    private DBConnection() {
    }

    /*
     * initiates a new connection to database or returns the existing connection
     * @param {}
     * @returns java.sql.Connection
     */

    public static Connection getDBConnection() {
        if (lastConnectionRequestedAt == null)
            DBConnection.setConnection();
        else if (new Date().getTime() - lastConnectionRequestedAt.getTime() > 240000)
            DBConnection.setConnection();
        return connection;
    }

    /*
     * initiates a new connection to database
     * @param {}
     * @returns void
     */
    private static void setConnection() {
        /*
         * local mysql database credentials
         */
        String url = "jdbc:mysql://localhost/";
        String dbname = "ishop_db";
        String ssl = "?useSSL=false";
        String username = "ishop-user";
        String password = "root";

        try {
                lastConnectionRequestedAt = new Date();
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url + dbname + ssl, username, password);
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }

    }
}
