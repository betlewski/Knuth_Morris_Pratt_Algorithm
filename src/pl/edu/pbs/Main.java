package pl.edu.pbs;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    /**
     * Wygenerowanie tablicy prefikso-sufiksów
     *
     * @param pattern poszukiwany wzorzec
     * @return długość najdłuższej ramki dla każdego podprefiksu wzorca
     */
    public static List<Integer> getPatternTable(String pattern) {
        List<Integer> TPS = new ArrayList<>();
        // Element tablicy o indeksie 0. przyjmuje wartość 0 (słowo jednoliterowe nie posiada prefikso-sufiksu właściwego):
        TPS.add(0, 0);
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) { // wartości tablicy obliczane są dla kolejnych podprefiksów wzorca
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                // Jeśli kolejny znak wzorca nie przedłuża poprzedniej ramki,
                // podjęta zostaje próba przedłużenia ramki poprzedniego prefikso-sufiksu.
                // Wynika to z właściwości redukcji prefikso-sufiksów:
                j = TPS.get(j - 1);
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                // Jeśli kolejny znak wzorca przedłuża poprzednią ramkę,
                // do tablicy wstawiana jest długość poprzedniej ramki zwiększona o 1.
                // Wynika to z właściwości rozszerzenia prefikso-sufiksów:
                j++;
            }
            TPS.add(i, j); // wstawienie do tablicy długości najdłuższej ramki dla podprefiksu wzorca
        }
        System.out.println("\nTablica prefikso-sufiksów:");
        System.out.println(TPS.stream()
                .map(String::valueOf).collect(Collectors.joining("-")));
        return TPS; // zwrócenie tablicy zawierającej długość najdłuższej ramki dla każdego podprefiksu wzorca
    }

    /**
     * Wyszukiwanie podłańcucha algorytmem Knutha-Morrisa-Pratta
     *
     * @param text    przeszukiwany tekst
     * @param pattern poszukiwany wzorzec
     * @return tablica pozycji wystąpienia wzorca
     */
    public static List<Integer> runKmpAlgorithm(String text, String pattern) {
        List<Integer> results = new ArrayList<>();
        List<Integer> TPS = getPatternTable(pattern); // wygenerowanie tablicy prefikso-sufiksów
        int j = 0;
        int i = 0;
        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                // Dopasowanie wzorca w określonej pozycji tekstu trwa,
                // dopóki kolejne znaki wzorca odpowiadają kolejnym znakom tekstu:
                j++;
                i++;
            }
            if (j == pattern.length()) {
                // Jeśli liczba zgodnych znaków jest równa rozmiarowi wzorca, dopasowanie jest pełne:
                results.add(i - j);
                // Kolejne poszukiwanie rozpoczyna się od elementu następującego po najdłuższej ramce wzorca,
                // wynika to z faktu, że najdłuższy sufiks jest jednocześnie najdłuższym prefiksem wzorca:
                j = TPS.get(j - 1);
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                // Jeśli podczas przeszukiwania łańcucha występuje niedopasowanie,
                // kolejna próba rozpoczyna się od elementu kończącego najdłuższą ramkę dopasowanej części wzorca:
                if (j > 0)
                    j = TPS.get(j - 1);
                else
                    i = i + 1;
            }
        }
        return results; // zwrócenie tablicy zawierającej pozycje wystąpienia wzorca
    }

    /**
     * Wyszukiwanie podłańcucha metodą indexOf do porównania z algorytmem KMP
     *
     * @param text    przeszukiwany tekst
     * @param pattern poszukiwany wzorzec
     * @return tablica pozycji wystąpienia wzorca
     */
    public static List<Integer> checkIndexOfResults(String text, String pattern) {
        List<Integer> results = new ArrayList<>();
        int index = text.indexOf(pattern);
        while (index != -1) {
            results.add(index);
            index = text.indexOf(pattern, index + 1);
        }
        return results;
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Podaj łańcuch znaków do przeszukania:");
        String text = reader.next();
        System.out.println("Podaj wzorzec do odszukania:");
        String pattern = reader.next();

        if (pattern.length() > 0 && text.length() > pattern.length()) {
            List<Integer> kmpResults = runKmpAlgorithm(text, pattern);
            System.out.println("\nWyniki algorytmu KMP:");
            System.out.println(kmpResults.stream()
                    .map(String::valueOf).collect(Collectors.joining("-")));

            System.out.println("\nWyniki indexOf():");
            List<Integer> indexOfResults = checkIndexOfResults(text, pattern);
            System.out.println(indexOfResults.stream()
                    .map(String::valueOf).collect(Collectors.joining("-")));

            System.out.println("\nPorównanie wyników:");
            if (kmpResults.equals(indexOfResults)) {
                System.out.println("ZGODNE");
            } else {
                System.out.println("NIEZGODNE");
            }
        } else {
            System.out.println("Podano niewłaściwe dane!");
        }
    }

}
