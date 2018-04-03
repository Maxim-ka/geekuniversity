import generatorWord.WordGenerator;
import phoneBook.PhoneBook;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final int MIN_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 10;
    private static final int MAX_CHAR = 1071;
    private static final int MIN_CHAR = 1040;
    private static final int LOT = 30;
    private static final int NUMBER_WORDS_IN_LINE = 10;
    private static final int LENGTH_PHONE_NUMBER = 10;
    private static final int MAX_NUMERAL = 57;
    private static final int MIN_NUMERAL = 48;

    public static void main(String[] args) {

        WordGenerator wordGenerator = new WordGenerator(MIN_CHAR, MAX_CHAR, MIN_WORD_LENGTH, MAX_WORD_LENGTH);
        String[] strings = wordGenerator.createArrayOfStrings(LOT, true);
        showWords(strings);
        System.out.println("Task_1 __________________________");
        getListAndNumberOfUniqueWordsUsingHashSet(strings);
        getListAndNumberOfUniqueWordsUsingHashMap(strings);

        System.out.println("Task_2 __________________________");
        PhoneBook phoneBook = new PhoneBook();
        WordGenerator generatorPhoneNumber = new WordGenerator(MIN_NUMERAL, MAX_NUMERAL, LENGTH_PHONE_NUMBER, LENGTH_PHONE_NUMBER);
        for (String string : strings) {
            phoneBook.add(string, generatorPhoneNumber.generateString());
        }
        System.out.println("вывод телефона по фамилии:");
        phoneBook.showPhone(strings[ThreadLocalRandom.current().nextInt(strings.length)]);
    }

    private static void showWords(String[] strings) {
        System.out.println("Массив слов:");
        for (int i = 0; i < strings.length; i++) {
            System.out.printf("%s ", strings[i]);
            if ((i + 1) % NUMBER_WORDS_IN_LINE == 0) System.out.println();
        }
        System.out.println();
    }

    private static void getListAndNumberOfUniqueWordsUsingHashSet(String[] strings){
        System.out.println("Получение списка и количество уникальных слов через HashSet:");
        HashSet<String> hashSet = new HashSet<>(Arrays.asList(strings));
        for (String str : hashSet) {
            int count = 0;
            for (String string : strings) {
                if (str.equals(string)) count++;
            }
            System.out.printf("%-10s - %d\n", str, count);
        }
        System.out.println();
    }

    private static void getListAndNumberOfUniqueWordsUsingHashMap(String[] strings ) {
        System.out.println("Получение списка и количество уникальных слов через HashMap:");
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String string : strings) {
            int count = (hashMap.getOrDefault(string, 0));
            hashMap.put(string, ++count);
        }
        for (Map.Entry<String, Integer> it: hashMap.entrySet()){
            System.out.printf("%-10s - %d\n", it.getKey(), it.getValue());
        }
        System.out.println();
    }
}
