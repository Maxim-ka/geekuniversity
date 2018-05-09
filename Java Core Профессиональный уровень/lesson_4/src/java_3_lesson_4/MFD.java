package java_3_lesson_4;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MFD {

    private static final int DELAY = 50;
    private static final String PRINT = "печать";
    private static final String SCAN = "скан";
    private final Scanner scanner = new Scanner(System.in);

    void launch(){
        String[] str;
        int size;
        do {
            System.out.println("Формат команды: действие кол-во действие кол-во ... действие кол-во");
            System.out.println("действие - печать или скан");
            str = readCommand();
            size = str.length;
            if (size % 2 != 0) System.out.printf("Неверно задан формат команды - %d слова\n", size);
        }while (size % 2 != 0);
        ExecutorService executorService = Executors.newFixedThreadPool(size / 2);
        scanner.close();
        for (int i = 0; i < size ; i += 2) {
            if (!str[i].equals(PRINT) && !str[i].equals(SCAN)){
                System.out.printf("Неверная команда \"%s\"\n", str[i]);
                continue;
            }
            final String activity = (str[i].equals(PRINT)) ? PRINT : SCAN;
            final int lotOfPage = getLotOfPage(str[i + 1]);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (activity){
                        for (int i = 0; i < lotOfPage; i++) {
                            try {
                                Thread.sleep(DELAY);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.printf("%s %d страниц\n",
                                    (activity.equals(PRINT)) ? "Отпечано" : "Отсканировано", i + 1);
                        }
                    }
                }
            });
        }
        executorService.shutdown();
    }

    private int getLotOfPage(String string){
        int num = 0;
        try {
            num = Integer.parseInt(string);
        }catch (NumberFormatException e){
            System.out.printf("Неверное количество страниц документа \"%s\"\n", string);
        }
        return num;
    }

    private String[] readCommand(){
        return scanner.nextLine().trim().toLowerCase().split("\\s+");
    }
}
