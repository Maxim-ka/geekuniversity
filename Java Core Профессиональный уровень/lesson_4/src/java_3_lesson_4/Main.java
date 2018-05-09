package java_3_lesson_4;

import java.io.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int NUMBER_OF_TIMES = 5;
    private static final Object ob = new Object();
    private static final String[] letters = {"A", "B", "C"};
    private static volatile int currentIndex;

    private static final int LEN_STRING = 50;
    private static final int NUMBER_STRING = 10;
    private static final int DELAY = 20;
    private static final int MIN_ENGLISH_LETTERS = 65;
    private static final int MAX_ENGLISH_LETTERS = 90;
    private static final int MIN_RUSSIAN_LETTERS = 1040;
    private static final int MAX_RUSSIAN_LETTERS = 1071;
    private static final int MIN_DIGIT_SYMBOL = 48;
    private static final int MAX_DIGIT_SYMBOL = 57;
    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("task_1");
        int size = letters.length;
        CountDownLatch count = new CountDownLatch(size);
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        for (int i = 0; i < size; i++) {
            final int index = i;
            executorService.execute(() -> {
                for (int j = 0; j < NUMBER_OF_TIMES; j++) {
                    synchronized (ob){
                        while (currentIndex != index){
                            try {
                                ob.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.print(letters[index]);
                        currentIndex++;
                        if (currentIndex == size) currentIndex = 0;
                        ob. notifyAll();
                    }
                }
                count.countDown();
            });
        }
        executorService.shutdown();
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("task_2");
        String[] text = {getText(MIN_ENGLISH_LETTERS, MAX_ENGLISH_LETTERS),
                getText(MIN_RUSSIAN_LETTERS, MAX_RUSSIAN_LETTERS),
                getText(MIN_DIGIT_SYMBOL, MAX_DIGIT_SYMBOL)};
        File file;
        if ((file = new File("task_2.txt")).exists()) file.delete();
        runTask_2(file, text);
        System.out.printf("смотри файл %s\n", file);
        System.out.println("task_3");
        new MFD().launch();
    }

    private static void runTask_2(File file, String[] strings ){
        int size = strings.length;
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        for (int i = 0; i < size; i++) {
            final int index = i;
            executorService.execute(() -> {
                try (PrintWriter fileWriter = new PrintWriter
                        (new BufferedWriter(new FileWriter(file, true), LEN_STRING), true)){
                    for (int j = 0; j < NUMBER_STRING; j++) {
                        Thread.sleep(DELAY);
                        synchronized (ob) {
                            while (currentIndex != index) {
                                ob.wait();
                            }
                            fileWriter.printf("%d) %s\n", j + 1, strings[index]);
                            currentIndex++;
                            if (currentIndex == size) currentIndex = 0;
                            ob.notifyAll();
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
    }

    private static String getText(int min, int max){
        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < LEN_STRING; i++) {
            textBuilder.append((char)(min + random.nextInt(max - min)));
        }
        return textBuilder.toString();
    }
}
