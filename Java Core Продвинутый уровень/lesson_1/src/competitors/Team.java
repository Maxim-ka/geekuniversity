package competitors;

import obstacles.Courses;
import obstacles.MyObstacles;
import obstacles.Obstacle;

public class Team {
    private Participantable[] participants;
    private String name;

    public Team(String name, Participantable... participants ){
        this.name = name;
        this.participants = participants;
    }

    public Participantable[] getParticipants() {
        return participants;
    }

    public void doIt(MyObstacles myObstacle){
        for (Participantable participant : participants) {
            switch (myObstacle){
                case CROSS:
                    participant.run(myObstacle.getSizeOfObstacle());
                    break;
                case WALL:
                    participant.jump(myObstacle.getSizeOfObstacle());
                    break;
                case WATER:
                    participant.swim(myObstacle.getSizeOfObstacle());
                    break;
                default:
                    System.out.println("Неизвестное препятствие");
            }
        }
    }

    public void showResults(){
        System.out.printf("%s результаты:\n", name);
        int win = 0;
        for (Participantable participant : participants) {
            if (participant.isOnDistance()) {
                participant.showResult();
                win++;
            }
        }
        if (win == 0) System.out.println("Никто не выжил");
    }

    public void recover(){
        for (Participantable participant : participants) {
            participant.setOnDistance(true);
        }
    }
}
