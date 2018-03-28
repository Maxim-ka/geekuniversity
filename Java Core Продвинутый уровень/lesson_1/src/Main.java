import competitors.*;
import obstacles.*;

import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final int MAX_CROSS = 500;
    private static final int MAX_WALL = 2;
    private static final int MAX_WATER = 50;
    private static final int NUMBER_OF_PARTICIPANTS = 4;
    private static final String NUMBER = "номер_";
    private static final int TYPE_CLASS = 3;
    private static final String TEAM_B = "Команда Б";

    public static void main(String[] args) {
        Courses courses = new Courses(new Cross(MAX_CROSS), new Wall(MAX_WALL), new Water(MAX_WATER));
        Team team = new Team(TEAM_B, toFormTeam());
        courses.doIt(team);
        team.showResults();

        System.out.println();
        System.out.println("Моя полоса через enum:");
        team.recover();
        for (MyObstacles myObstacle: MyObstacles.values()) {
            team.doIt(myObstacle);
        }
        team.showResults();
    }

    private static Participantable[] toFormTeam() {
        Participantable[] participants = new Participantable[NUMBER_OF_PARTICIPANTS];
        for (int i = 0; i < NUMBER_OF_PARTICIPANTS; i++) {
            switch (ThreadLocalRandom.current().nextInt(TYPE_CLASS)) {
                case 0:
                    participants[i] = new Cat(NUMBER + (i + 1),
                            generatingSkill(Cat.MAX_RUN), generatingSkill(Cat.MAX_JUMP));
                    break;
                case 1:
                    participants[i] = new Dog(NUMBER + (i + 1),
                            generatingSkill(Dog.MAX_RUN), generatingSkill(Dog.MAX_JUMP),
                            generatingSkill(Dog.MAX_SWIM));
                    break;
                case 2:
                    participants[i] = new Human(NUMBER + (i + 1),
                            generatingSkill(Human.MAX_RUN), generatingSkill(Human.MAX_JUMP),
                            generatingSkill(Human.MAX_SWIM));
            }
        }
        return participants;
    }

    private static int generatingSkill(int max){
        return ThreadLocalRandom.current().nextInt(max + 1);
    }
}
