
class MyThread extends Thread {
    private float[] array;
    private int start;
    private int end;
    private boolean v2;

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
        v2 = true;
        start();
    }

    @Override
    public void run() {
        for (int i = start; i < end ; i++) {
            if (v2) if (array[i] == 0) continue; // TODO: 07.04.2018 эта строка только ради метода, c разделением массива методом класса System
            array[i] = Main.toCalculateNewValue(array[i], i);
        }
    }
}
