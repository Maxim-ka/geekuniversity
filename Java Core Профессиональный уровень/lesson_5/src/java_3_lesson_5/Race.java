package java_3_lesson_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class Race {

    private final ReentrantLock win  = new ReentrantLock();                   //добавление

    private ArrayList<Stage> stages;

    public ArrayList<Stage> getStages(){
        return stages;
    }

    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }

    ReentrantLock getWin(){
        return win;
    }
}
