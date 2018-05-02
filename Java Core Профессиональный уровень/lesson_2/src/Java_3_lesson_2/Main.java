package Java_3_lesson_2;

import java.sql.*;

public class Main {
    private static final String CREATE_TABLE = "CREATE TABLE products (" +
                                                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                "prodid TEXT NOT NULL," +
                                                "title TEXT NOT NULL," +
                                                "cost INTEGER NOT NULL);";
    private static final String CLEAR = "DELETE FROM products;";
    private static final String DELETE_SIZE = "VACUUM;";
    private static final String RESET_AUTOINCREMENT = "Update SQLITE_SEQUENCE SET seq = 0 " +
                                                       "WHERE name = 'products';";
    private static final String FILL = "INSERT INTO products (prodid, title, cost) VALUES (?, ?, ?);";
    private static Connection connection;

    public static void main(String[] args) {
        try {
            connect();
            createTable();
            fillInTableProducts();
            Console console = new Console(connection);
            do {
                console.actionSelection();
            }while (console.getExecution());
        }catch (Exception err){
            err.printStackTrace();
        }finally {
            disconnect();
        }
    }

    private static void fillInTableProducts() throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FILL)){
            for (int i = 0; i < 10000; i++) {
                preparedStatement.setString(1, String.format("id_товара %d", i + 1));
                preparedStatement.setString(2, String.format("товар%d", i + 1));
                preparedStatement.setInt(3, (i + 1) * 10);
                preparedStatement.addBatch();
            }
            connection.setAutoCommit(false);
            preparedStatement.executeBatch();
        }catch (SQLException e){
            connection.rollback();
            System.out.println("Не удалось заполнить таблицу");
            e.printStackTrace();
        }finally {
            connection.setAutoCommit(true);
        }
    }

    private static void createTable(){
        try (Statement statement = connection.createStatement()){
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet table = metaData.getTables(null, null, "products", null);
            if (table.next()){
                table.close();
                statement.addBatch(CLEAR);
                statement.addBatch(DELETE_SIZE);
                statement.addBatch(RESET_AUTOINCREMENT);
                statement.executeBatch();
            }else statement.executeUpdate(CREATE_TABLE);
        }catch (SQLException e){
            System.out.println("Проблемы с созданием таблицы");
            e.printStackTrace();
        }
    }

    private static void connect()throws Exception{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:database.db");
    }

    private static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
