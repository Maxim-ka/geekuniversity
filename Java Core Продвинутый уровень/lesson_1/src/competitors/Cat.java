package competitors;

public class Cat extends Animal{
    private static final int SWIM_DISTANCE = 0;
    public static final int MAX_RUN = 100;
    public static final int MAX_JUMP = 3;

    public Cat(String name, int maxRunDistance, int maxJumpHeight) {
        super("Кот", name, maxRunDistance, maxJumpHeight, SWIM_DISTANCE);
    }
}

