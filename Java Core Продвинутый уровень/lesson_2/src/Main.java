import lesson_2.MethodTwoDimensionalArray;
import lesson_2.MyArrayDataException;
import lesson_2.MyArraySizeException;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static final int SIZE_LINE = 4;
    private static final int SIZE_COL = 4;
    private static final int MAX_NUMBER = 100;
    private static final int RANDOM_MIN_COL = 0;
    private static final int RANDOM_MAX_COL = 5;
    private static final int RANDOM_STRING = 4;
    private static final String STRING = "ab";

    public static void main(String[] args) {
        try {
            System.out.printf("Сумма элементов массива %d\n",
                    new MethodTwoDimensionalArray(SIZE_LINE, SIZE_COL)
                            .getSumOfTwoDimensionalArray(generateStringArray()));
        }catch (MyArraySizeException | MyArrayDataException e) {
            e.printStackTrace();
        }
    }

    private static String generateStringArray(){
        StringBuilder stringBuilder = new StringBuilder();
        int size = ThreadLocalRandom.current()
                .nextInt(SIZE_COL * SIZE_LINE - SIZE_LINE, SIZE_COL * SIZE_LINE + SIZE_LINE);
        for (int i = 0; i < size; i++) {
            if ((int)(Math.random() * MAX_NUMBER) == RANDOM_MIN_COL) stringBuilder.append(" ");
            else if ((int)(Math.random() * MAX_NUMBER) == RANDOM_STRING) stringBuilder.append(STRING);
            else stringBuilder.append(ThreadLocalRandom.current().nextInt(MAX_NUMBER));
            int extraColumn = ((int)(Math.random() * MAX_NUMBER) == RANDOM_MAX_COL) ? 0 : 1;
            if ((i + extraColumn)% SIZE_COL == 0)stringBuilder.append("\n");
            else stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

}
