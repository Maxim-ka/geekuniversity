package java_3_lesson_5;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import static java_3_lesson_5.MainClass.CARS_COUNT;

public abstract class Stage {

    protected final ReentrantLock win  = new ReentrantLock();                   //добавление
    protected final CountDownLatch start = new CountDownLatch(CARS_COUNT);      //добавление
    protected final CyclicBarrier barrierStart = new CyclicBarrier(CARS_COUNT); //добавление
    protected final CountDownLatch finish = new CountDownLatch(CARS_COUNT);     //добавление
    protected final Semaphore semaphore = new Semaphore(CARS_COUNT / 2);  //добавление

    protected int length;
    protected String description;

    public String getDescription() {
        return description;
    }

    public abstract void go(Car c);
}
