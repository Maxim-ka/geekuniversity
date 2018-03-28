package obstacles;

import competitors.Participantable;
import competitors.Team;

public class Courses {

    private  Obstacle[] obstacles;

    public Courses(Obstacle... obstacles) {
        this.obstacles = obstacles;
    }

    public Obstacle[] getObstacles() {
        return obstacles;
    }

    public void doIt (Team team){
        for (Obstacle obstacle : obstacles) {
            for (Participantable participantable : team.getParticipants()) {
                obstacle.doIt(participantable);
            }
        }
    }
}
