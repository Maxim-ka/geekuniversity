import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


public class ClassWithTestes {

    @Test(priority = 10)
    void testMethod10() throws NoSuchMethodException {
        System.out.println("выполнение теста 10");
    }

    @Test(priority = 2)
    void testMethod2() throws NoSuchMethodException {
        System.out.println("выполнение теста 2");
    }

    @Test(priority = 1)
    private void testMethod11() throws NoSuchMethodException {
        System.out.println("выполнение теста 1");
    }

    @Test(priority = 5)
    static void testMethod5() throws NoSuchMethodException {
        System.out.println("выполнение теста 5");
    }
    @Test(priority = 1)
    void testMethod1() throws NoSuchMethodException {
        System.out.println("выполнение теста 1");
    }
    @Test(priority = 9)
    void testMethod9() throws NoSuchMethodException {
        System.out.println("выполнение теста 9");
    }
    @Test(priority = 6)
    void testMethod6() throws NoSuchMethodException {
        System.out.println("выполнение теста 6");
    }
    @Test(priority = 3)
    void testMethod3() throws NoSuchMethodException {
        System.out.println("выполнение теста 3");
    }
    @Test(priority = 7)
    void testMethod7() throws NoSuchMethodException {
        System.out.println("выполнение теста 7");
    }
    @Test(priority = 4)
    private void testMethod4() throws NoSuchMethodException {
        System.out.println("выполнение теста 4");
    }
    @Test(priority = 6)
    static void testMethod61() throws NoSuchMethodException {
        System.out.println("выполнение теста 6");
    }

    @Test(priority = 8)
    void testMethod8() throws NoSuchMethodException {
        System.out.println("выполнение теста 8");
    }
    @AfterSuite
    static void testMethod() throws NoSuchMethodException {
        System.out.println("выполнение теста AfterSuite");
    }
    @BeforeSuite
    void testMethodBeforeSuite() throws NoSuchMethodException {
        System.out.println("выполнение теста BeforeSuite");
    }
//    @AfterSuite
//    void testMethodAfterSuite() throws NoSuchMethodException {
//        System.out.println("выполнение теста AfterSuite");
//    }
}
