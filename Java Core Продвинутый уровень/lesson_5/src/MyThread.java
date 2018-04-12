
class MyThread extends Thread {
    private float[] array;
    private int start;
    private int end;

    MyThread(float[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
        start();
    }

    MyThread(float[] array){
        this.array = array;
        start = 0;
        end = array.length;
        start();
    }

    @Override
    public void run() {
        for (int i = start; i < end ; i++) {
            if (array[i] != 0) array[i] = Main.toCalculateNewValue(array[i], i);
            // TODO: 07.04.2018 условие if (array[i] != 0) только ради метода,
            // c разделением массива методом класса System
        }
    }
}
