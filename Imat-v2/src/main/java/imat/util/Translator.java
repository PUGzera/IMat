package imat.util;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Translator {

    private Map<String, String> dictionary = new TreeMap<>(String::compareToIgnoreCase);

    private Translator() {
        dictionary.put("Balj", "pod");
        dictionary.put("Mjöl, Socker och Salt", "flour_sugar_salt");
        dictionary.put("Varma drickor", "hot_drinks");
        dictionary.put("Pasta", "pasta");
        dictionary.put("Sött", "sweet");
        dictionary.put("Mejerier","dairies");
        dictionary.put("Kål", "cabbage");
        dictionary.put("Potatis och Ris", "potato_rice");
        dictionary.put("Örter", "herb");
        dictionary.put("Kött", "meat");
        dictionary.put("Bröd", "bread");
        dictionary.put("Kalla drickor", "cold_drinks");
        dictionary.put("Frukt", "fruit");
        dictionary.put("Bär", "berry");
        dictionary.put("Meloner", "melons");
        dictionary.put("Exotiska Frukter", "exotic_fruit");
        dictionary.put("Nötter och Frön", "nuts_and_seeds");
        dictionary.put("Citrus Frukter", "citrus_fruit");
        dictionary.put("Rot Frukter", "root_vegetable");
        dictionary.put("Fisk", "fish");
        dictionary.put("Vegetabilisk Frukt", "vegetable_fruit");
    }

    public static String translate(String word) {
        return new Translator().dictionary.get(word);
    }

    public static Set<String> getCategories() {
        return new Translator().dictionary.keySet();
    }
}
