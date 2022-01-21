package pl.edu.pbs.service;

import java.util.*;

public class CharCounter {

    public static Map<Character, Integer> analyzeText(String text) {
        Map<Character, Integer> results = new HashMap<>();
        for (char character : text.toCharArray()) {
            if (results.containsKey(character)) {
                results.put(character, results.get(character) + 1);
            } else {
                results.put(character, 1);
            }
        }
        return results;
    }

}
