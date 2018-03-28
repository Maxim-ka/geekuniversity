package competitors;

public class Human implements Participantable{
    public static final int MAX_RUN = 1000;
    public static final int MAX_JUMP = 2;
    public static final int MAX_SWIM = 100;
    private String name;
    private int maxRunDistance;
    private int maxJumpHeight;
    private int maxSwimDistance;
    private boolean onDistance;

    public Human(String name, int maxRunDistance, int maxJumpHeight, int maxSwimDistance){
        this.name = name;
        this.maxRunDistance = maxRunDistance;
        this.maxJumpHeight = maxJumpHeight;
        this.maxSwimDistance = maxSwimDistance;
        this.onDistance = true;
    }

    public void setOnDistance(boolean onDistance) {
        this.onDistance = onDistance;
    }

    @Override
    public void run(int distance) {
        if (!onDistance) return;
        if (maxRunDistance >= distance) System.out.printf("%s кросс пройден\n", name);
        else {
            onDistance = false;
            System.out.printf("%s сдох на дороге\n", name);
        }
    }

    @Override
    public void swim(int distance) {
        if (!onDistance) return;
        if (maxSwimDistance >= distance) System.out.printf("%s удачно проплыл\n", name);
        else {
            onDistance = false;
            System.out.printf("%s утонул\n", name);
        }
    }

    @Override
    public void jump(int height) {
        if (!onDistance) return;
        if (maxJumpHeight == 0 && height != 0) System.out.printf("%s испугался высоты\n", name);
        if (maxJumpHeight >= height) System.out.printf("%s удачно преодолел препятствие\n", name);
        else {
            onDistance = false;
            System.out.printf("%s не смог перелезть\n", name);
        }
    }

    @Override
    public boolean isOnDistance() {
        return onDistance;
    }

    @Override
    public void showResult() {
        if (isOnDistance()) System.out.printf("%s прошел полосу препятствий\n", name);
        else System.out.printf("%s остался на полосе\n", name);
    }
}
