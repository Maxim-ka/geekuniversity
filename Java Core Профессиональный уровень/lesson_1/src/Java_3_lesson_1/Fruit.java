package Java_3_lesson_1;

abstract class Fruit {
    abstract String getName();
    abstract float getWeightFruit();
}

class Apple extends Fruit{
    private static final float WEIGHT_APPLE = 1.0f;
    private static final String NAME = "Яблок";

    String getName() {
        return NAME;
    }

    @Override
    float getWeightFruit() {
        return WEIGHT_APPLE;
    }
}

class Orange extends Fruit{
    private static  final float WEIGHT_ORANGE = 1.5f;
    private static final String NAME = "Апельсин";

    String getName() {
        return NAME;
    }

    @Override
    float getWeightFruit() {
        return WEIGHT_ORANGE;
    }
}