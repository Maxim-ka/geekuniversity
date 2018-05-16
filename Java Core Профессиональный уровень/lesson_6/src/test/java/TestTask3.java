import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.sql.*;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
/*
Я исходил из того, что JUNIT тестовые методы должны быть независимы друг от друга
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class TestTask3 {
    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList (new Object[][]{
                {"Студент_1", -1, 1},
                {"Студент_2", -2, 1},
                {"Студент_3", -3, 1},
                {"Студент_4", -4, 1},
                {"Студент_5", -5, 1}
        });
    }
    private String surname;
    private int score;
    private int result;

    public TestTask3(String surname, int score, int result){
        this.surname = surname;
        this.score = score;
        this.result = result;
    }

    private static Connection connection;
    private static Statement statement;
    private static Savepoint savepoint;

    @BeforeClass
    public static void starTestBD(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:task3.db");
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            savepoint = connection.setSavepoint();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int insert() throws SQLException {
        return statement.executeUpdate(
                String.format("INSERT INTO students (surname, score) VALUES ('%s', %d);", surname, score));
    }

    private int update() throws SQLException {
        return statement.executeUpdate(
                String.format("UPDATE students SET score = %d WHERE surname = '%s';", score * 2, surname));
    }

    private int readScore() throws SQLException {
        return statement.executeQuery(String.format("SELECT score FROM students WHERE surname = '%s';", surname))
                .getInt("score");
    }

    private String readSurname() throws SQLException {
        return statement.executeQuery(String.format("SELECT surname FROM students WHERE score = %d;", score))
                .getString("surname");
    }

    @Before
    public void writeToBD() throws SQLException {
        insert();
    }

    @Test
    public void testCorrectInsertIntoBD() throws SQLException {
        Assert.assertEquals(result, insert());
        Assert.assertEquals(score, readScore());
    }

    @Test
    public void testCorrectReadFromBD() throws SQLException {
        Assert.assertEquals(score, readScore());
        Assert.assertEquals(surname, readSurname());
    }

    @Test
    public void testCorrectUpdateBD() throws SQLException {
        Assert.assertEquals(result, update());
        Assert.assertEquals(score * 2, readScore());
    }

    @After
    public void deleteBD() throws SQLException {
        statement.executeUpdate(String.format("DELETE FROM students WHERE surname = '%s';", surname));
    }

    @AfterClass
    public static void endTestBD(){
        try {
            connection.rollback(savepoint);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
