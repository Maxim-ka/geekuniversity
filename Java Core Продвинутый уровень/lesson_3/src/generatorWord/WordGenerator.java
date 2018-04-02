package generatorWord;

import java.util.concurrent.ThreadLocalRandom;

public class WordGenerator {

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

    public String generateString(){
        if (stringBuilder.length() != 0) stringBuilder.delete(0, stringBuilder.length());
        int size = ThreadLocalRandom.current().nextInt(minLenWord, maxLenWord + 1);
        for (int j = 0; j < size; j++) {
            stringBuilder.append((char) (ThreadLocalRandom.current().nextInt(minChar, maxChar + 1)));
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
        int random;
        for (int i = 0; i < strings.length; i++) {
            do {
                random = ThreadLocalRandom.current().nextInt(strings.length);
                if (!strings[random].equals(strings[i])){
                    strings[i] = strings[random];
                }
            }while (i != random);
        }
        return strings;
    }
}
