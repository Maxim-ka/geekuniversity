import java.sql.*;

public class Main {
    private static Connection connection;
    private static Statement statement;
    private static String[] studentsSurname = {"Иванов", "Петров", "Сидоров", "Орлов", "Табуреткин"};

    public static void main(String[] args) {
        try {
            connect();
            createTable();
            clearTable();
            fillInTableProducts();
            disconnect();
        }catch (Exception err){
            err.printStackTrace();
        }
    }

    private static void fillInTableProducts() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO students (surname, score) VALUES (?, ?);");
        for (int i = 0; i < studentsSurname.length; i++) {
            preparedStatement.setString(1, studentsSurname[i]);
            preparedStatement.setInt(2, (int)(1 + Math.random() * studentsSurname.length));
            preparedStatement.addBatch();
        }
        connection.setAutoCommit(false);
        preparedStatement.executeBatch();
        connection.setAutoCommit(true);

    }

    private static void clearTable() throws SQLException {
        statement.executeUpdate("DELETE FROM students;");
        statement.executeUpdate("VACUUM;");
        statement.executeUpdate("Update SQLITE_SEQUENCE SET seq = 0 WHERE name = 'students';");

    }

    private static void createTable() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "surname TEXT NOT NULL,"+
                "score INTEGER NOT NULL);");
    }

    private static void connect()throws Exception{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:task3.db");
        statement = connection.createStatement();
    }

    private static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
