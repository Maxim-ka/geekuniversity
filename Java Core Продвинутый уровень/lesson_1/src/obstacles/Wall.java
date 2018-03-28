package obstacles;

import competitors.Participantable;

public class Wall extends Obstacle {

    private int height;

    public Wall(int height) {
        this.height = (int) (Math.random() * height);
    }

    @Override
    void doIt(Participantable participant) {
        participant.jump(height);
    }
}
