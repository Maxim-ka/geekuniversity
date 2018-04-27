package Java_3_lesson_1;

import java.util.ArrayList;

class Box<T extends Fruit> {

    private ArrayList<T> arrayList;

    Box(){
        arrayList = new ArrayList<>();
    }

    float getWeight(){
        return (arrayList.isEmpty()) ? 0 : arrayList.size() * getTypeFruit().getWeightFruit();
    }

    void addFruit(T fruit){
        arrayList.add(fruit);
    }

    void pourFruit(Box<T> another){
        if (arrayList.isEmpty()) {
            System.out.println("Пересыпать нечего, корзина пуста");
            return;
        }
        another.arrayList.addAll(arrayList);
        System.out.printf("пересыпали %d штук %s\n", arrayList.size(), getTypeFruit().getName());
        arrayList.clear();
        arrayList.trimToSize();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        Box box = (Box) obj;
        return Math.abs(this.getWeight() - box.getWeight()) < 0.00001f;
    }

    void compare (Box another){
        if (this.equals(another)) System.out.println("коробки с одинаковым весом");
        else System.out.println("коробки с разным весом");
    }

    private T getTypeFruit(){
        return arrayList.get(0);
    }
}