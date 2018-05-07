package java_3_lesson_3;

import java.io.*;
import java.util.Scanner;

class Console {

    private static final float IN_SEC = 0.001f;
    private static final String AHEAD = "вперед - V";
    private static final String BACKWARD = "назад - N";
    private final Scanner scanner = new Scanner(System.in);
    private int numPage = -1;
    private int page;
    private String charset;
    private int oldPage;

    Console(int page, String charset) {
        this.page = page;
        this.charset = charset;
    }

    void launch(){
        String stringPath;
        do {
            System.out.println("Вести путь к файлу для чтения, выход - Q");
            stringPath = scanner.nextLine().trim();
            if (!stringPath.equalsIgnoreCase("Q")){
                if (new File(stringPath).exists()) toReadFile(stringPath);
                else System.out.println("Такого файла нет");
            }
        }while (!stringPath.equalsIgnoreCase("Q"));
        scanner.close();
        System.out.println("Выход");
    }

    private void toReadFile(String nameFile){
        long startLoadTime = System.currentTimeMillis();
        String textPage = null;
        byte[] buffer = new byte[page];
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(nameFile, "r")){
            long size = randomAccessFile.length();
            if (size == 0){
                System.out.printf("файл %s пуст\n", nameFile);
                return;
            }
            long numPages = (size % page == 0) ? size / page : size / page + 1;
            System.out.printf("%f сек, время на загрузку\n",
                    (System.currentTimeMillis() - startLoadTime) * IN_SEC);
            do {
                getNumberPage(numPages);
                if (numPage > 0){
                    long startReadTime = System.currentTimeMillis();
                    if (numPage != oldPage){
                        randomAccessFile.seek((numPage - 1) * page);
                        int end = randomAccessFile.read(buffer);
                        textPage = new String(buffer, 0 , end, charset);
                    }
                    System.out.println(textPage);
                    System.out.printf("%d из %d\n", numPage, numPages);
                    System.out.printf("%f сек, время на чтение\n",
                            (System.currentTimeMillis() - startReadTime) * IN_SEC);
                }
            }while (numPage > 0);
            numPage = -1;
            oldPage = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getNumberPage(long numPages){
        do{
            String ahead = AHEAD, backward = BACKWARD;
            if (numPage <= 1) backward = "";
            if (numPage == numPages) ahead = "";
            System.out.printf("Листать %s %s или вести номер страницы из %d страниц, выход - 0\n", ahead, backward, numPages);
            String string = scanner.nextLine().trim().toUpperCase();
            switch (string){
                case "V":
                    if (!ahead.equals("")) numPage = (numPage == -1) ? 1: ++numPage;
                    break;
                case "N":
                    if (!backward.equals("")) numPage--;
                    break;
                default:
                    try {
                        oldPage = numPage;
                        numPage = Integer.parseInt(string);
                        if (numPage > numPages){
                            System.out.printf("Такой %d страницы нет\n", numPage);
                            numPage = oldPage;
                        }
                    }catch (NumberFormatException e){
                        System.out.printf("Введеный номер страницы \"%s\" не является числом\n", string);
                    }
            }
        }while (numPage < 0);
    }
}
