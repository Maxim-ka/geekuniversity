#### Урок 3
## Средства ввода-вывода
Обзор средств ввода-вывода. Байтовые, символьные,
буферизованные потоки. Сетевое взаимодействие,
сериализация и десериализация объектов.
   
Домашнее задание
1. Прочитать файл (около 50 байт) в байтовый массив и вывести этот массив в консоль;
2. Последовательно сшить 5 файлов в один (файлы примерно 100 байт). Может пригодиться
следующая конструкция:
    ```
    ArrayList<InputStream> al = new ArrayList<>();
    ...
    Enumeration<InputStream> e = Collections.enumeration(al);
    ```
3. Написать консольное приложение, которое умеет постранично читать текстовые файлы
(размером > 10 mb). Вводим страницу (за страницу можно принять 1800 символов), программа
выводит ее в консоль. Контролируем время выполнения: программа не должна загружаться
дольше 10 секунд, а чтение – занимать свыше 5 секунд.

    Чтобы не было проблем с кодировкой, используйте латинские буквы. 

Использованы русские буквы, применена однобайтовая кодировка CP1251

вариант кода для 2 задания

```
private static void runTask_2(){
    byte[] arrByte = new byte[SIZE_BYTES];
    ArrayList<InputStream> inputStreamArrayList = new ArrayList<>();
    try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream
            (new FileOutputStream("task_2.txt")))){
        for (File nameFile : nameFiles) {
            inputStreamArrayList.add(new FileInputStream(nameFile));
        }
        SequenceInputStream seqInputStream = new SequenceInputStream(Collections.enumeration(inputStreamArrayList));
        while (seqInputStream.read(arrByte) != -1){
            out.write(arrByte);
            out.writeUTF("\n");
            out.flush();
            System.out.println(new String(arrByte));
        }
        seqInputStream.close();
        System.out.println("Задание 2 выполнено, см файл task_2.txt");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```      
