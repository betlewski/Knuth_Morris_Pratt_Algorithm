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

        String filename = "random-text.txt"; // zmień nazwę pliku źródłowego
        AlgorithmResult algorithmResult = new AlgorithmResult(filename);
        String text = fileService.readFile(filename);

        List<String> patterns = getRandomTextPatterns(); // zmień metodę pobierającą wzorce dla danego pliku
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

    public static List<String> getSarsGenomePatterns() {
        return Arrays.asList("CGGCT",
                "AGGAGCTGGT",
                "CATATCAGCATCTATAGTAGCTGGTGGTATTGTAGCTATCGTAGTAACAT",
                "GAGAAGGCATTAAAATATTTGCCTATAGATAAATGTAGTAGAATTATACCTGCACGTGCTCGTGTAGAGTGTTTTGATAAATTCAAAGTGAATTCAACAT",
                "TAGCTGCATTTCACCAAGAATGTAGTTTACAGTCATGTACTCAACATCAACCATATGTAGTTGATGACCCGTGTCCTATTCACTTCTATTCTAAATGGTATATTAGAGTAGGAGCTAGAAAATCAGCACCTTTAATTGAATTGTGCGTGGATGAGGCTGGTTCTAAATCACCCATTCAGTACATCGATATCGGTAATTATACAGTTTCCTGTTTACCTTTTACAATTAATTGCCAGGAACCTAAATTGGGTAGTCTTGTAGTGCGTTGTTCGTTCTATGAAGACTTTTTAGAGTATCATGACGTTCGTGTTGTTTTAGATTTCATCTAAACGAACAAACTAAAATGTCTGATAATGGACCCCAAAATCAGCGAAATGCACCCCGCATTACGTTTGGTGGACCCTCAGATTCAACTGGCAGTAACCAGAATGGAGAACGCAGTGGGGCGCGATCAAAACAACGTCGGCCCCAAGGTTTACCCAATAATACTGCGTCTTGGT");
    }

    public static List<String> getLoremIpsumPatterns() {
        return Arrays.asList("lorem",
                "ipsum dolo",
                "olor sit amet consectetur adipiscing elit pellente",
                "dui vivamus arcu. Faucibus ornare suspendisse sed nisi lacus sed. Fermentum odio eu feugiat pretium.",
                "Mattis vulputate enim nulla aliquet. Nulla at volutpat diam ut. Pulvinar sapien et ligula ullamcorper malesuada proin. Urna id volutpat lacus laoreet non curabitur gravida arcu ac. Leo integer malesuada nunc vel risus commodo viverra maecenas. Aliquam vestibulum morbi blandit cursus risus. Nunc mi ipsum faucibus vitae aliquet nec ullamcorper sit. Dictum non consectetur a erat. In mollis nunc sed id. Fames ac turpis egestas maecenas pharetra. Pellentesque habitant morbi tristique senectus et netu");
    }

    public static List<String> getPanTadeuszPatterns() {
        return Arrays.asList("okien",
                "tylko się ",
                "Krwią i mózgiem splamione. Trójkąt w sztuki pryska",
                "Oto nowy marszałek na ręku stronnikówWyniesion z refektarza. Patrz, jak szlachta bratyRzucają czapki",
                "Litwo! Ojczyzno moja! ty jesteś jak zdrowie:" +
                        "Ile cię trzeba cenić, ten tylko się dowie," +
                        "Kto cię stracił. Dziś piękność twą w całej ozdobie" +
                        "Widzę i opisuję, bo tęsknię po tobie." +
                        "    Panno święta, co Jasnej bronisz Częstochowy" +
                        "I w Ostrej świecisz Bramie! Ty, co gród zamkowy" +
                        "Nowogródzki ochraniasz z jego wiernym ludem!" +
                        "Jak mnie dziecko do zdrowia powróciłaś cudem" +
                        "(Gdy od płaczącej matki, pod Twoją opiekę" +
                        "Ofiarowany, martwą podniosłem powiekę;" +
                        "I zaraz mogłem pieszo, do Twych świątyń progu" +
                        "Iść za wrócone życie ");
    }

    public static List<String> getSylabusISTPatterns() {
        return Arrays.asList("punkt",
                "PRZEDMIOTU",
                "Odniesienie docharakterystykII stopnia(kod składni",
                "zakresie wysokopoziomowegoprogramowania bezzałogowych statków powietrznychma rozszerzoną i podbudowa",
                "Analiza bezpieczeństwa internetowych kont bankowych" +
                        "Projekt algorytmu szyfrowania i jego implementacja." +
                        "Projekt zabezpieczeń programowych dla systemu Windows - konfiguracja" +
                        "bezpiecznego środowiska" +
                        "Projekt zabezpieczeń programowych dla systemu Android - konfiguracja" +
                        "bezpiecznego środowiska" +
                        "Analiza funkcjonalna stron internetowych - monitorowanie i zestawienie adresacji" +
                        "serwerów zbierających informację o użytkowniku." +
                        "Projekt środowiska programowo-sprzętowego monitorującego aktywność aplikacji" +
                        "i systemu and");
    }

    public static List<String> getRandomTextPatterns() {
        return Arrays.asList("ahjji",
                "bgtyutreah",
                "abgc tooot otuy abc abc abcde ahjji ootp naoa abcd",
                "abcde ahjji ootp naoa abcd abgc atooot otuy abc abc abcde ahjji ootp naoa abcd abgc atooot otuy abc ",
                "abc abcde ahjji ootp naoa abcd abgc atooot otuy abc abc abcde ahjji ootp naoa abcd abgc atooot otuy abc" +
                        "abc abcde ahjji ootp naoa abcd abgc atooot otuy abc abc abcde ahjji ootp naoa abcd abgc atooot otuy abc" +
                        "abc abcde ahjji ootp naoa abcd abgc atooot otuy abc abc abcde ahjji ootp naoa abcd abgc atooot otuy abc" +
                        "abc abcde ahjji ootp naoa abcd abgc atooot otuy abc abc abcde ahjji ootp naoa abcd abgc atooot otuy abc" +
                        "abc abcde ahjji ootp naoa abcd abgc atooot otuy abc abc abcde ahjji ootp naoa abcd abgc ");
    }
}
