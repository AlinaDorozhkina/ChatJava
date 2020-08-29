import com.mysql.cj.jdbc.Driver;

import java.sql.*;


public class MainData {
    private Connection connection;
    private String url;
    private String user;
    private String password;

    public MainData(String url, String user, String password) {

        this.url = url;
        this.password = password;
        this.user = user;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found");
        }

        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("Driver Registration error");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

