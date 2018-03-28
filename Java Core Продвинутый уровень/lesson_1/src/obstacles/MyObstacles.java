package obstacles;

public enum MyObstacles { //2-ая полоса препятствий эксперемент

    CROSS(500), WALL(2), WATER(50);

    private int sizeOfObstacle;

    MyObstacles(int sizeOfObstacle) {
        this.sizeOfObstacle = (int) (Math.random() * sizeOfObstacle);
    }

    public int getSizeOfObstacle() {
        return sizeOfObstacle;
    }
}
