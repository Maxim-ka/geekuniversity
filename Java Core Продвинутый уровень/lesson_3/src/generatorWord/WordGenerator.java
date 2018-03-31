package generatorWord;

import java.util.concurrent.ThreadLocalRandom;

public class WordGenerator {

    private int lenPhoneNumber;
    private int minChar;
    private int maxChar;
    private int minLenWord;
    private int maxLenWord;
    private final StringBuilder stringBuilder = new StringBuilder();

    public WordGenerator(int minChar, int maxChar, int minLenWord, int maxLenWord) {
        this.minChar = minChar;
        this.maxChar = maxChar;
        this.minLenWord = minLenWord;
        this.maxLenWord = maxLenWord;
    }

    public WordGenerator(int lenPhoneNumber) {
        this.lenPhoneNumber = lenPhoneNumber;
    }

    public String generateString(){
        if (stringBuilder.length() != 0) stringBuilder.delete(0, stringBuilder.length());
        int size = (lenPhoneNumber != 0)? lenPhoneNumber : ThreadLocalRandom.current().nextInt(minLenWord, maxLenWord);
        for (int j = 0; j < size; j++) {
            if (lenPhoneNumber != 0) stringBuilder.append(ThreadLocalRandom.current().nextInt(lenPhoneNumber));
            else stringBuilder.append((char) (ThreadLocalRandom.current().nextInt(minChar, maxChar + 1)));
        }
        return stringBuilder.toString();
    }

    public String[] createArrayOfStrings(int size, boolean duplicate){
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = generateString();
        }
        if (duplicate) strings = addDuplicateWords(strings);
        return strings;
    }

    private String[] addDuplicateWords(String[] strings ){
        for (int i = 0; i < strings.length; i++) {
            do {
                int random = ThreadLocalRandom.current().nextInt(strings.length);
                if (i == random) break;
                if (!strings[random].equals(strings[i])){
                    strings[i] = strings[random];
                }
            }while (true);
        }
        return strings;
    }
}
