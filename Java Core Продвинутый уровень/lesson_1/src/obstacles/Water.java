package obstacles;

import competitors.Participantable;

public class Water extends Obstacle {

    private int distance;

    public Water(int distance) {
        this.distance = (int) (Math.random() * distance);
    }

    @Override
    void doIt(Participantable participant) {
        participant.swim(distance);
    }
}
