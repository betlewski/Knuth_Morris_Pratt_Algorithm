package pl.edu.pbs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        KMPAlgorithm kmpAlgorithm = new KMPAlgorithm();
        SearchTextJava searchTextJava = new SearchTextJava();
        FileService fileService = new FileService();

        String filename = "sars-cov-2-genome-sequence.txt";
        AlgorithmResult algorithmResult = new AlgorithmResult(filename);

        String text = fileService.readFile(filename);
        List<String> patterns = Arrays.asList("CGGCT",
                "AGGAGCTGGT",
                "CATATCAGCATCTATAGTAGCTGGTGGTATTGTAGCTATCGTAGTAACAT",
                "GAGAAGGCATTAAAATATTTGCCTATAGATAAATGTAGTAGAATTATACCTGCACGTGCTCGTGTAGAGTGTTTTGATAAATTCAAAGTGAATTCAACAT",
                "TAGCTGCATTTCACCAAGAATGTAGTTTACAGTCATGTACTCAACATCAACCATATGTAGTTGATGACCCGTGTCCTATTCACTTCTATTCTAAATGGTATATTAGAGTAGGAGCTAGAAAATCAGCACCTTTAATTGAATTGTGCGTGGATGAGGCTGGTTCTAAATCACCCATTCAGTACATCGATATCGGTAATTATACAGTTTCCTGTTTACCTTTTACAATTAATTGCCAGGAACCTAAATTGGGTAGTCTTGTAGTGCGTTGTTCGTTCTATGAAGACTTTTTAGAGTATCATGACGTTCGTGTTGTTTTAGATTTCATCTAAACGAACAAACTAAAATGTCTGATAATGGACCCCAAAATCAGCGAAATGCACCCCGCATTACGTTTGGTGGACCCTCAGATTCAACTGGCAGTAACCAGAATGGAGAACGCAGTGGGGCGCGATCAAAACAACGTCGGCCCCAAGGTTTACCCAATAATACTGCGTCTTGGT");
        algorithmResult.addHeader(patterns);

        List<Long> timesKMPAlgorithm = new ArrayList<>();
        List<Long> timesIndexOf = new ArrayList<>();

        for (int i = 0; i < patterns.size(); i++) {
            String pattern = patterns.get(i);
            System.out.println("Wyniki dla wzorca (" + pattern.length() + "): " + pattern);

            long startKMPAlgorithm = System.nanoTime();
            List<Integer> kmpResults = kmpAlgorithm.runAlgorithm(text, pattern);
            long finishKMPAlgorithm = System.nanoTime();
            long timeKMPAlgorithm = finishKMPAlgorithm - startKMPAlgorithm;
            timesKMPAlgorithm.add(timeKMPAlgorithm);
            System.out.println("\nWyniki algorytmu KMP:");
            System.out.println(kmpResults.stream()
                    .map(String::valueOf).collect(Collectors.joining("-")));
            System.out.println("Czas: " + timeKMPAlgorithm + " ns");

            long startIndexOf = System.nanoTime();
            List<Integer> indexOfResults = searchTextJava.checkIndexOfResults(text, pattern);
            long finishIndexOf = System.nanoTime();
            long timeIndexOf = finishIndexOf - startIndexOf;
            timesIndexOf.add(timeIndexOf);
            System.out.println("\nWyniki indexOf():");
            System.out.println(indexOfResults.stream()
                    .map(String::valueOf).collect(Collectors.joining("-")));
            System.out.println("Czas: " + timeIndexOf + " ns");

            System.out.println("\nPorównanie wyników:");
            if (kmpResults.equals(indexOfResults)) {
                System.out.println("ZGODNE");
            } else {
                System.out.println("NIEZGODNE");
            }
        }

        algorithmResult.addLine(timesKMPAlgorithm);
        algorithmResult.addLine(timesIndexOf);

        fileService.writeFile(algorithmResult.getFilename(), algorithmResult.getLines());
    }
}
