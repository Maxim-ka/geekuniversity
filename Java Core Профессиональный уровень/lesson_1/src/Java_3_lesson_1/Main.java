package Java_3_lesson_1;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    private static final int NUMBER_10 = 10;
    private static final int NUMBER_20 = 20;

    private static Integer[] ints = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
    private static Character[] characters = new Character[]{'A', 'B', 'C', 'D', 'E', 'F'};
    private static String[] strings = new String[]{"xxx", "zzz", "ccc", "vvv", "aaa"};

    public static void main(String[] args) {

        System.out.println("Задание 1");
        System.out.println(Arrays.toString(ints));
        System.out.println(Arrays.toString(changeElementTask_1(ints, 2,8)));
        System.out.println();
        System.out.println(Arrays.toString(characters));
        System.out.println(Arrays.toString(changeElementTask_1(characters, 'F','A')));
        System.out.println();
        System.out.println(Arrays.toString(strings));
        System.out.println(Arrays.toString(changeElementTask_1(strings, "zzz","aaa")));
        System.out.println();
        System.out.println("Задание 2");
        System.out.println(convertArrayToArrayListTask_2(ints));
        System.out.println(convertArrayToArrayListTask_2(characters));
        System.out.println(convertArrayToArrayListTask_2(strings));
        System.out.println();
        System.out.println("Задание 3");

        Box<Apple> box1 = new Box<>();
        Box<Orange> box2 = new Box<>();
        Box<Apple> box3 = new Box<>();

        for (int i = 0; i < NUMBER_10; i++) {
            box1.addFruit(new Apple());
        }
        System.out.printf("вес 1-0й корзины c %d яблоками = %.2f\n", NUMBER_10, box1.getWeight());

        for (int i = 0; i < NUMBER_20; i++) {
            box2.addFruit(new Orange());
        }
        System.out.printf("вес 2-ой корзины c %d апельсинами = %.2f\n", NUMBER_20, box2.getWeight());

        for (int i = 0; i < NUMBER_20; i++) {
            box3.addFruit(new Apple());
        }
        System.out.printf("вес 3-ой корзины c %d яблоками = %.2f\n", NUMBER_20, box3.getWeight());

        box3.pourFruit(box1);
        System.out.printf("вес 1-ой корзины c  яблоками = %.2f\n", box1.getWeight());
        System.out.printf("вес 3-ой корзины c  яблоками = %.2f\n", box3.getWeight());

        box1.compare(box2);
        box3.compare(box2);
        box1.compare(box3);
    }

    private static <T> ArrayList<T> convertArrayToArrayListTask_2(T[] array){
        return new ArrayList<>(Arrays.asList(array));
    }

    private static <T> T[] changeElementTask_1(T[] array, T element_1, T element_2){
        if (element_1 == element_2){
            System.out.println("Элементы одинаковые");
            return array;
        }
        if (array.length < 2) {
            System.out.println("В массиве меньше двух элементов");
            return array;
        }
        int index_1 = getIndexOfElementInArray(array, element_1);
        if (index_1 == -1){
            System.out.println("В массиве отсутствует " + element_1);
            return array;
        }
        int index_2 = getIndexOfElementInArray(array, element_2);
        if (index_2 == -1){
            System.out.println("В массиве отсутствует " + element_2);
            return array;
        }
        T tmp = array[index_1];
        array[index_1] = array[index_2];
        array[index_2] = tmp;
        return array;
    }

    private static <T> int getIndexOfElementInArray(T[] array, T element){
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) return i;
        }
        return -1;
    }
}
