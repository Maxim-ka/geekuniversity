package competitors;

public interface Participantable {
    void run(int distance);
    void swim(int distance);
    void jump(int height);
    boolean isOnDistance();
    void showResult();
    void setOnDistance(boolean onDistance);
}
