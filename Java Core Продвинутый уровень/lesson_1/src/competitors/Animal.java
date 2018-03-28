package competitors;

abstract class Animal implements Participantable {
    private String type;
    private String name;
    private int maxRunDistance;
    private int maxJumpHeight;
    private int maxSwimDistance;
    private boolean onDistance;

    Animal(String type, String name, int maxRunDistance, int maxJumpHeight, int maxSwimDistance) {
        this.type = type;
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
    public boolean isOnDistance() {
        return onDistance;
    }

    public void run(int distance){
        if (!onDistance) return;
        if (maxRunDistance >= distance) System.out.printf("%s %s кросс пройден\n", type, name);
        else {
            onDistance = false;
            System.out.printf("%s %s сдох на дороге\n", type, name);
        }
    }

    public void jump(int height){
        if (!onDistance) return;
        if (maxJumpHeight >= height) System.out.printf("%s %s удачно преодолел препятствие\n", type, name);
        else {
            onDistance = false;
            System.out.printf("%s %s не смог перепрыгнуть\n",  type, name);
        }
    }

    public void swim(int distance){
        if (!onDistance) return;
        if (maxSwimDistance == 0){
            System.out.printf("%s %s Не полез в воду\n", type, name);
            onDistance = false;
            return;
        }
        if (maxSwimDistance >= distance) System.out.printf("%s %s переплыл\n", type, name);
        else {
            onDistance = false;
            System.out.printf("%s %s утонул\n", type, name);
        }
    }

    public void showResult(){
        if (isOnDistance()) System.out.printf("%s %s прошел полосу препятствий\n", type, name);
        else System.out.printf("%s %s остался на полосе\n", type, name);
    }
}
