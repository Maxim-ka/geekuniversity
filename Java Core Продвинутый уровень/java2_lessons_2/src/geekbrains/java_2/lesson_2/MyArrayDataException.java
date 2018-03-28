package geekbrains.java_2.lesson_2;

import java.lang.Exception;

public class MyArrayDataException extends Exception {
    private String str;
    private int i;
    private int j;
    private StringBuilder arr;

    MyArrayDataException(String[][] strings, String str){
        super();
        this.str = str;
        arr = new StringBuilder("\n");
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings.length; j++) {
                if (str.equals(strings[i][j])){
                    this.i = i;
                    this.j = j;
                }
                arr.append(strings[i][j].concat("\t"));
            }
            arr.append("\n");
        }
    }


    @Override
    public String toString() {
        return String.format("Элемент \"%s\" %d-й строки и %d-го столбца массива не является числом: %s",
                str, i + 1, j + 1, arr);
    }
}
