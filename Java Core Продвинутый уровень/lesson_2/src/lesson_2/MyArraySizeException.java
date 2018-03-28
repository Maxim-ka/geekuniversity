package lesson_2;

public class MyArraySizeException extends Exception {

    private static final String INCORRECT_NUMBER_OF_ROWS = "Некорректное количество строк ";
    private static final String INCORRECT_NUMBER_OF_COLUMNS = "Некорректное количество столбцов ";
    private String message;
    private int givenSize;
    private int length;
    private String column = "\t";
    private StringBuilder arr;

    MyArraySizeException(int givenSize, String strings[], String column){
        this.givenSize = givenSize;
        length = (column == null) ? strings.length : (column.split("\\s+")).length ;
        message = (column == null) ? INCORRECT_NUMBER_OF_ROWS : INCORRECT_NUMBER_OF_COLUMNS;
        arr = new StringBuilder("\n");
        for (int i = 0; i < strings.length; i++) {
            if (column != null && column.equals(strings[i])){
                this.column = "(" + column + ") из " + (i + 1) + "-ой строки ";
            }
            arr.append(strings[i]).append("\n");
        }
    }

    @Override
    public String toString() {
        return message + column + length +
                ((givenSize > length) ? " < " + givenSize : " > " + givenSize) + arr;
    }
}
