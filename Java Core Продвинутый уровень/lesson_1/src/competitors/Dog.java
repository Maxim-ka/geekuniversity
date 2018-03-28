package competitors;

public class Dog extends Animal {
    public static final int MAX_RUN = 500;
    public static final int MAX_JUMP = 2;
    public static final int MAX_SWIM = 50;

    public Dog(String name, int maxRunDistance, int maxJumpHeight, int maxSwimDistance) {
        super("Пес", name, maxRunDistance, maxJumpHeight, maxSwimDistance);
    }
}
