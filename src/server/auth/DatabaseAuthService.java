package server.auth;

import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class DatabaseAuthService implements AuthService {
    private Connection connection;
    private Statement statement;

    @Override
    public void start() {
connection();
    }

    private void connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Java/Chat/my.db");
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        String nick = null;
        try {
            ResultSet rs = statement.executeQuery("SELECT nick_name FROM users WHERE user_name ='"+ login +
                    "' AND password='"+pass+ "';");

           nick = rs.getString("nick_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(nick);
        return nick;
    }
}
