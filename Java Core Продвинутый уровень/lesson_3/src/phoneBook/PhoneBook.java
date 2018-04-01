package phoneBook;

import java.util.ArrayList;
import java.util.TreeMap;

public class PhoneBook{

    private static final String NOT_CONTACT = "Такого контакта нет";
    private final TreeMap<String, ArrayList<String>> contacts = new TreeMap<>();

    public void add(String name, String phoneNumber){
        ArrayList<String> listPhone = contacts.getOrDefault(name, new ArrayList<>());
        if (listPhone.add(phoneNumber)) contacts.put(name, listPhone);
    }

    public void get(String name){
        if (contacts.containsKey(name)){
            for (String phone : contacts.get(name)) {
                System.out.printf("%s : %s\n", name, phone);
            }
        } else System.out.printf("%s - %s\n",name, NOT_CONTACT);
    }
}
