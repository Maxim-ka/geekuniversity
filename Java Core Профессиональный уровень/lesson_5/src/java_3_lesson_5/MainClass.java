package java_3_lesson_5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        CountDownLatch start = new CountDownLatch(CARS_COUNT);      //добавление
        CyclicBarrier barrierStart = new CyclicBarrier(CARS_COUNT); //добавление
        CountDownLatch finish = new CountDownLatch(CARS_COUNT);     //добавление
        Semaphore semaphore = new Semaphore(CARS_COUNT / 2);  //добавление
        Race race = new Race(new Road(60), new Tunnel(semaphore), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {   //изменение
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), barrierStart, start, finish);
            new Thread(cars[i]).start();
        }
        try {
            start.await();              //добавление
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            finish.await();             //добавление
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
