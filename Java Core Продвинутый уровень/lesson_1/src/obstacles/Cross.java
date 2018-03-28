package obstacles;

import competitors.Participantable;

public class Cross extends Obstacle {

    private int distance;

    public Cross(int distance) {
        this.distance = (int) (Math.random() * distance);
    }

    @Override
    void doIt(Participantable participant) {
        participant.run(distance);
    }
}
