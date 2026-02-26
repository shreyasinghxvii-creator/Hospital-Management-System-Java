import java.sql.Connection;       // for Connection interface
import java.sql.DriverManager;    // to establish connection

public class DBConnection {

    // Database URL
    // format: jdbc:postgresql://localhost:port/databaseName
    // here my database name is Hospital
    private static final String URL = 
        "jdbc:postgresql://localhost:5432/Hospital";

    // my postgres username
    private static final String USER = "postgres";

    // my postgres password
    private static final String PASSWORD = "shreya";

    // this method will return connection object
    public static Connection getConnection() {

        try {

            // loading PostgreSQL driver class
            // without this sometimes connection does not work
            Class.forName("org.postgresql.Driver");

            // creating connection using URL, USER, PASSWORD
            Connection con = 
                DriverManager.getConnection(URL, USER, PASSWORD);

            // returning connection to whoever calls this method
            return con;

        } catch (Exception e) {

            // printing error if connection fails
            // helpful for debugging
            System.out.println("Database connection error:");
            e.printStackTrace();

            return null;
        }
    }

    // I made this main method just to test if connection works
    // this is not required in final project but useful for checking
    public static void main(String[] args) {

        Connection con = getConnection();

        if (con != null) {

            System.out.println("Connected Successfully!");

        } else {

            System.out.println("Connection Failed!");
        }
    }
}