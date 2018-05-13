package java_3_lesson_5;

import java.util.concurrent.BrokenBarrierException;

public class Car implements Runnable {

    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            race.getStart().countDown();                                   // добавление
            for (int i = 0; i < race.getStages().size(); i++) {
                if (i == 0) race.getStages().get(i).barrierStart.await();  // добавление
                race.getStages().get(i).go(this);
                if (i == (race.getStages().size() - 1)){                   // добавление
                    race.getFinish().countDown();                          // добавление
                    if (race.getStages().get(i).win.tryLock()){            // добавление
                        System.out.printf(" Победитель гонки %s\n", name); // добавление
                        race.getFinish().await();                          // добавление
                        race.getStages().get(i).win.unlock();              // добавление
                    }
                }
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

