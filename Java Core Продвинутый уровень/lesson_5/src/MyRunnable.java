
class MyRunnable implements Runnable {

    private float[] array;
    private int index;

    MyRunnable(float[] array, int index){
        this.array = array;
        this.index = index;
    }

    @Override
    public void run() {
        int size = array.length;
        for (int i = 0; i < size; i++) {
            array[i] = Main.toCalculateNewValue(array[i], (index == 0) ? i: index + i);
        }
    }
}
