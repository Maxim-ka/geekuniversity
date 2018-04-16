package onJavaFX.server;

import java.sql.*;

public class AuthService {

    private Connection connection;
    private Statement statement;

    void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:chatDataBase.db");
        statement = connection.createStatement();
    }

    void  disconnect(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String getNick(String login, String password){
        String request = "SELECT nickname FROM users WHERE login= '" + login.toLowerCase()
                + "' AND password= '" + password.toLowerCase() + "'";
        try (ResultSet resultSet = statement.executeQuery(request)){
            if (resultSet.next()) return resultSet.getString("nickname");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
