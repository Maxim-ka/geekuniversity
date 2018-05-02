package Java_3_lesson_2;

import java.sql.*;
import java.util.Scanner;

class Console {

    private static final String PRICE = "/цена";
    private static final String CHANGE_PRICE = "/сменитьцену";
    private static final String PRODUCTS_BY_PRICE = "/товарыпоцене";
    private static final String EXIT = "/выход";
    private static final String PRICE_REQUEST = "SELECT title, cost FROM products WHERE title= ?;";
    private static final String REQUEST_CHANGE_RATES = "UPDATE products SET cost = ? WHERE title = ?;";
    private static final String REQUEST_PRODUCTS_AT_PRICES = "SELECT * FROM products WHERE cost BETWEEN ? AND ?;";
    private final Scanner scanner = new Scanner(System.in);
    private final Connection connection;
    private final PreparedStatement preRequestPrice;
    private final PreparedStatement preRequestPriceChange;
    private final PreparedStatement preInquiryOfGoodsByPrices;
    private String[] str;
    private boolean execution = true;

    Console(Connection connection) throws SQLException {
        this.connection = connection;
        preRequestPrice = connection.prepareStatement(PRICE_REQUEST);
        preRequestPriceChange = connection.prepareStatement(REQUEST_CHANGE_RATES);
        preInquiryOfGoodsByPrices = connection.prepareStatement(REQUEST_PRODUCTS_AT_PRICES);
    }

    void actionSelection() throws SQLException{
        System.out.println("Ввести команду, (выход - /выход):");
        str = readCommand();
        switch (str[0]){
            case PRICE:
                toPriceOfGoods();
                break;
            case CHANGE_PRICE:
                int price = getNumber(str[str.length - 1]);
                if (price == -1) return;
                toChangePrice(price);
                break;
            case PRODUCTS_BY_PRICE:
                if (str.length == 1 || (str.length - 1) % 2 != 0) {
                    System.out.println("Некорретное число аргументов команды");
                    return;
                }
                showProductForPrice();
                break;
            case EXIT:
                execution = false;
                preRequestPrice.close();
                preRequestPriceChange.close();
                preInquiryOfGoodsByPrices.close();
                scanner.close();
                System.out.println("Выход");
                break;
            default:
                System.out.printf("\"%s\" - Не является командой\n", str[0]);
        }
    }

    boolean getExecution() {
        return execution;
    }

    private void toPriceOfGoods()throws SQLException{
        for (int i = 1; i < str.length; i++) {
            preRequestPrice.setString(1, str[i]);
            ResultSet resultSet = preRequestPrice.executeQuery();
            if (resultSet.isClosed())System.out.printf("Такого товара \"%s\" нет\n", str[i]);
            while (resultSet.next()){
                System.out.printf("%s - %d\n", resultSet.getString("title"), resultSet.getInt("cost"));
            }
        }
        preRequestPrice.clearParameters();
    }

    private void toChangePrice(int newPrice)throws SQLException{
        for (int i = 1; i < str.length - 1; i++) {
            preRequestPriceChange.setInt(1, newPrice);
            preRequestPriceChange.setString(2, str[i]);
            preRequestPriceChange.addBatch();
        }
        connection.setAutoCommit(false);
        Savepoint savepoint = connection.setSavepoint();
        int[] reply = preRequestPriceChange.executeBatch();
        if (isUpdatesPriceFault(reply)) connection.rollback(savepoint);
        connection.setAutoCommit(true);
        preRequestPriceChange.clearParameters();
    }

    private void showProductForPrice()throws SQLException{
        int[] price = getPrice();
        if (price == null) return;
        for (int i = 0; i < price.length; i += 2) {
            preInquiryOfGoodsByPrices.setInt(1, price[i]);
            preInquiryOfGoodsByPrices.setInt(2, price[i + 1]);
            ResultSet rs = preInquiryOfGoodsByPrices.executeQuery();
            if (rs.isClosed()) System.out.printf("в указаном диапазоне цен %d и %d, товаров нет\n", price[i], price[i + 1]);
            while (rs.next()){
                System.out.printf("%s : %s - %d\n", rs.getString("prodid"), rs.getString("title"),
                        rs.getInt("cost"));
            }
        }
        preInquiryOfGoodsByPrices.clearParameters();
    }

    private boolean isUpdatesPriceFault(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0){
                System.out.printf("товар \"%s\" не найден\n", str[i + 1]);
                System.out.println("проверяйте правильность вводимых значений");
                return true;
            }
        }
        System.out.println("Обновление цены прошло удачно");
        return false;
    }

    private String[] readCommand(){
        return scanner.nextLine().trim().toLowerCase().split("\\s+");
    }

    private int[] getPrice(){
        int[] num = new int[str.length - 1];
        for (int i = 1; i < str.length ; i++) {
            num[i - 1] = getNumber(str[i]);
            if (num[i - 1] == -1) return null;
        }
        return sortMinMax(num);
    }

    private int[] sortMinMax(int[] arr){
        for (int i = 0; i < arr.length; i += 2) {
            if (arr[i + 1] < arr[i]){
                int tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
            }
        }
        return arr;
    }

    private int getNumber(String string){
        int num = -1;
        try {
            num = Integer.parseInt(string);
        }catch (NumberFormatException err){
            System.out.printf("Ошибка, \"%s\" не является ценой\n", string);
        }
        return num;
    }
}
