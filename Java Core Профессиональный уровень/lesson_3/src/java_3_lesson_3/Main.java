package java_3_lesson_3;

import java.io.*;
import java.util.*;

public class Main {

    private static final int SIZE_BYTES = 100;
    private static final int NUMBER_FILES = 5;
    private static final String UTF_8 = "UTF-8";
    private static final String CP1251 = "Cp1251";
    private static final Random random = new Random();
    private static final int MIN_ENGLISH_LETTERS = 65;
    private static final int MAX_ENGLISH_LETTERS = 90;
    private static final int MIN_RUSSIAN_LETTERS = 1040;
    private static final int MAX_RUSSIAN_LETTERS = 1071;
    private static final int PAGE = 1800;
    private static final int LENGTH_OF_TEXT = 3_000_000;
    private static File[] nameFiles;
    private static String charset = CP1251;

    public static void main(String[] args) {
        createMultipleFileNames(NUMBER_FILES);
        System.out.println("task 1");
        runTask_1(getRandomFile());
        System.out.println();
        System.out.println("task 2");
        runTask_2();
        System.out.println("task 3");
        runTask_3(getRandomFile());
        new Console(PAGE, charset).launch();
    }

    private static void runTask_3(File file){
        System.out.printf("Файл для 3 задания %s\n", file);
        fillFile(file, createText(true, LENGTH_OF_TEXT, MIN_RUSSIAN_LETTERS, MAX_RUSSIAN_LETTERS));
    }

    private static void runTask_2(){
        byte[] arrByte = new byte[SIZE_BYTES];
        ArrayList<InputStream> inputStreamArrayList = new ArrayList<>();
        try (PrintWriter printWriter = new PrintWriter
                (new BufferedWriter(new OutputStreamWriter(new FileOutputStream("task_2.txt"), UTF_8)), true)){
             for (File nameFile : nameFiles) {
                inputStreamArrayList.add(new FileInputStream(nameFile));
            }
            SequenceInputStream seqInputStream = new SequenceInputStream(Collections.enumeration(inputStreamArrayList));
            while (seqInputStream.read(arrByte) != -1){
                String string = new String(arrByte, charset);
                printWriter.println(string);
                System.out.println(string);
            }
            seqInputStream.close();
            System.out.println("Задание 2 выполнено, см файл task_2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createMultipleFileNames(int numberFiles){
        nameFiles = new File[numberFiles];
        for (int i = 0; i < numberFiles; i++) {
            nameFiles[i] = new File(String.format("file_%d.txt", i + 1));
            nameFiles[i].deleteOnExit();
            fillFile(nameFiles[i], createText(false, SIZE_BYTES, MIN_RUSSIAN_LETTERS,
                    MAX_RUSSIAN_LETTERS));
        }
    }

    private static void runTask_1(File nameFile){
        System.out.printf("чтение из файла %s\n", nameFile);
        byte[] arrByte = new byte[SIZE_BYTES / 2];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream
                (new FileInputStream(nameFile), SIZE_BYTES);
             BufferedReader bufferedReader = new BufferedReader
                     (new InputStreamReader(new ByteArrayInputStream(arrByte), charset), SIZE_BYTES)){
            bufferedInputStream.read(arrByte, 0, arrByte.length);
            System.out.println(Arrays.toString(arrByte));
            System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getRandomFile(){
        return nameFiles[(1 + random.nextInt(NUMBER_FILES - 1))];
    }

    private static void fillFile(File nameFile, String text){
        try (BufferedWriter bufferedWriter = new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(nameFile), charset))){
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createText(boolean word, int len, int min, int max){
        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < len ; i = (word)? i + 1 : i + 2) {
            for (int j = 0; j < ((word) ? random.nextInt(10) + 1 : 1); j++) {
                textBuilder.append((char)(min + random.nextInt(max - min)));
            }
            textBuilder.append(" ");
        }
        return textBuilder.toString();
    }
}
