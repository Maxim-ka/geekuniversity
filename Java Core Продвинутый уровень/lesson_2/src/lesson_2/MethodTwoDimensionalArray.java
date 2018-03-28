package lesson_2;

import java.util.Arrays;

public class MethodTwoDimensionalArray {

    private int line;
    private int column;

    public MethodTwoDimensionalArray(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getSumOfTwoDimensionalArray(String string) throws MyArraySizeException, MyArrayDataException {
        String[] strings = string.trim().split("\n");
        if (strings.length != line)
            throw new MyArraySizeException(line, strings, null);
        return calculateSum(checkColumnArray(strings));
    }

    private String[][] checkColumnArray(String[] strings) throws MyArraySizeException {
        String[][] str = new String[strings.length][];
        for (int i = 0; i < strings.length; i++) {
            String[] columns = strings[i].split("\\s+|^$?");
            if (columns.length != column)
                throw  new MyArraySizeException(column, strings, strings[i]);
            str[i] = columns;
        }
        return str;
    }

    private int calculateSum(String[][] arrString ) throws MyArrayDataException {
        int sum = 0;
        for (int i = 0; i < arrString.length; i++) {
            for (int j = 0; j < arrString.length; j++) {
                try {
                    sum += Integer.parseInt(arrString[i][j]);
                }catch (NumberFormatException e){
                    throw (MyArrayDataException)
                            new MyArrayDataException(arrString, arrString[i][j]).initCause(e);
                }
            }
        }
        System.out.println(Arrays.deepToString(arrString));
        return sum;
    }
}
