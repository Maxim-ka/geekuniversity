package java_3_lesson_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Race {


    private ArrayList<Stage> stages;

    public ArrayList<Stage> getStages(){
        return stages;
    }

    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
    }

    CountDownLatch getStart() {         //добавление
        return stages.get(0).start;
    }

    CountDownLatch getFinish() {         //добавление
        return stages.get(stages.size() - 1).finish;
    }
}
