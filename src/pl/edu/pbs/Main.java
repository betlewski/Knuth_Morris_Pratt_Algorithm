package pl.edu.pbs;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        KMPAlgorithm kmpAlgorithm = new KMPAlgorithm();
        SearchTextJava searchTextJava = new SearchTextJava();
        FileService fileService = new FileService();

        String text = fileService.readFile("sars-cov-2-genome-sequence.txt");
        String pattern = "CGGCT";

        Instant timeBeforeKMP = Instant.now();
        List<Integer> kmpResults = kmpAlgorithm.runAlgorithm(text, pattern);
        Instant timeAfterKMP = Instant.now();
        System.out.println("\nWyniki algorytmu KMP:");
        System.out.println(kmpResults.stream()
                .map(String::valueOf).collect(Collectors.joining("-")));
        System.out.println(Duration.between(timeBeforeKMP, timeAfterKMP).toMillis());

        Instant timeBeforeIndexOf = Instant.now();
        List<Integer> indexOfResults = searchTextJava.checkIndexOfResults(text, pattern);
        Instant timeAfterIndexOf = Instant.now();
        System.out.println("\nWyniki indexOf():");
        System.out.println(indexOfResults.stream()
                .map(String::valueOf).collect(Collectors.joining("-")));
        System.out.println(Duration.between(timeBeforeIndexOf, timeAfterIndexOf).toMillis());

        System.out.println("\nPorównanie wyników:");
        if (kmpResults.equals(indexOfResults)) {
            System.out.println("ZGODNE");
        } else {
            System.out.println("NIEZGODNE");
        }
    }

}
