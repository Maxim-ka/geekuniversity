
import java.util.Arrays;

public class Main {

    private static final int SIZE = 10_000_000;
    private static final int HALF = SIZE / 2;
    private static final float FILL = 1f;
    private static final float IN_SECONDS = 0.001f;

    public static void main(String[] args) {

        toCalculateFirstMethod();
        calculateWithoutSplittingArrayIntoTwoStreams();
        calculateDivisionOfArrayIntoTwo();
        calculateDivisionOfArrayIntoTwoV2();
    }

    private static void toCalculateFirstMethod(){
        float[] array = new float[SIZE];
        Arrays.fill(array, FILL);
        long launch = System.currentTimeMillis();
        for (int i = 0; i < array.length ; i++) {
            array[i] = toCalculateNewValue(array[i], i);
        }
        long completion = System.currentTimeMillis();
        System.out.printf("Время выполнения метода одним потоком %.3f сек\n",
                (completion - launch) * IN_SECONDS);
    }

    static float toCalculateNewValue(float oldValue, int index){
        return (float)(oldValue * Math.sin(0.2f + index / 5) * Math.cos(0.2f + index / 5)
                * Math.cos(0.4f + index / 2));
    }

    private static void calculateWithoutSplittingArrayIntoTwoStreams(){
        float[] array = new float[SIZE];
        Arrays.fill(array, FILL);
        long  launch = System.currentTimeMillis();
        MyThread flowOfCalculationOfFirstHalf = new MyThread(array, 0, HALF);
        MyThread calculationFlowOfSecondHalf = new MyThread(array, HALF, SIZE);
        try {
            flowOfCalculationOfFirstHalf.join();
            calculationFlowOfSecondHalf.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        long completion = System.currentTimeMillis();
        System.out.printf("Время выполнения метода, без разделения массива двумя потоками %.3f сек\n",
                (completion - launch) * IN_SECONDS);
    }

    private static void calculateDivisionOfArrayIntoTwo(){
        float[] array = new float[SIZE];
        Arrays.fill(array, FILL);
        long  launch = System.currentTimeMillis();
        float[] rstHalf = Arrays.copyOfRange(array, 0, HALF);
        float[] secondHalf = Arrays.copyOfRange(array, HALF, SIZE);
        Thread flowOfCalculationOfFirstHalf = new Thread(new MyRunnable(rstHalf, 0));
        Thread calculationFlowOfSecondHalf = new Thread(new MyRunnable(secondHalf, HALF));
        flowOfCalculationOfFirstHalf.start();
        calculationFlowOfSecondHalf.start();
        try {
            flowOfCalculationOfFirstHalf.join();
            calculationFlowOfSecondHalf.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.arraycopy(rstHalf, 0, array, 0, HALF);
        System.arraycopy(secondHalf, 0, array, HALF, HALF);
        long completion = System.currentTimeMillis();
        System.out.printf("Время выполнения метода, c разделением массива на два подмассива %.3f сек\n",
                (completion - launch) * IN_SECONDS);
    }

    private static void calculateDivisionOfArrayIntoTwoV2(){
        float[] array = new float[SIZE];
        Arrays.fill(array, FILL);
        long  launch = System.currentTimeMillis();
        float[] rstHalf = new float[HALF];
        float[] secondHalf = new float[SIZE];
        System.arraycopy(array, 0, rstHalf, 0, HALF);
        System.arraycopy(array, HALF, secondHalf, HALF, HALF);
        MyThread flowOfCalculationOfFirstHalf = new MyThread(rstHalf);
        MyThread calculationFlowOfSecondHalf = new MyThread(secondHalf);
        try {
            flowOfCalculationOfFirstHalf.join();
            calculationFlowOfSecondHalf.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.arraycopy(rstHalf, 0, array, 0, HALF);
        System.arraycopy(secondHalf, HALF, array, HALF, HALF);
        long completion = System.currentTimeMillis();
        System.out.printf("Время выполнения метода, c разделением массива методом класса System %.3f сек\n",
                (completion - launch) * IN_SECONDS);
    }
}
