package phoneBook;

import java.util.HashSet;
import java.util.TreeMap;

public class PhoneBook{

    private static final String NOT_CONTACT = "Такого контакта нет";
    private final TreeMap<String, HashSet<String>> contacts = new TreeMap<>();

    public void add(String name, String phoneNumber){
        if (!contacts.containsKey(name)) contacts.put(name, new HashSet<>());
        contacts.get(name).add(phoneNumber);
    }

    private HashSet<String> get(String name){
         return contacts.get(name);
    }

    public void showPhone(String name){
        if (contacts.containsKey(name)){
            for (String phone : get(name)) {
                System.out.printf("%s : %s\n", name, phone);
            }
        }else System.out.printf("%s - %s\n",name, NOT_CONTACT);
    }
}
