package geekbrains.java_2.lesson_2;

public class MyArraySizeException extends Exception {

    private String message;
    private int givenSize;
    private int length;
    private String column = "\t";
    private StringBuilder arr;

    MyArraySizeException(String message, int givenSize, String strings[], String column){
        this.message = message;
        this.givenSize = givenSize;
        length = (column == null) ? strings.length : (column.split("\\s+")).length ;
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
