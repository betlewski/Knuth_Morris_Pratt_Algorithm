package pl.edu.pbs;

import java.util.ArrayList;
import java.util.List;

public class SearchTextJava {

    /**
     * Wyszukiwanie podłańcucha metodą indexOf do porównania z algorytmem KMP
     *
     * @param text    przeszukiwany tekst
     * @param pattern poszukiwany wzorzec
     * @return tablica pozycji wystąpienia wzorca
     */
    public List<Integer> checkIndexOfResults(String text, String pattern) {
        List<Integer> results = new ArrayList<>();
        int index = text.indexOf(pattern);
        while (index != -1) {
            results.add(index);
            index = text.indexOf(pattern, index + 1);
        }
        return results;
    }

}
