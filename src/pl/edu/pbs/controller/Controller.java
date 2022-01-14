package pl.edu.pbs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.edu.pbs.algorithm.KMPAlgorithm;
import pl.edu.pbs.algorithm.SearchTextJava;
import pl.edu.pbs.service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    private TextField pathToFileLabel;
    @FXML
    private TextField searchPatternLabel;
    @FXML
    private TextArea resultsLabel;
    @FXML
    private Button searchBtn;

    private FileService fileService;
    private KMPAlgorithm kmpAlgorithm;
    private SearchTextJava searchTextJava;
    private List<String> results;

    public void initialize() {
        this.fileService = new FileService();
        this.kmpAlgorithm = new KMPAlgorithm();
        this.searchTextJava = new SearchTextJava();
        initSearchBtn();
    }

    public void initSearchBtn() {
        searchBtn.setOnAction(actionEvent -> {
            results = new ArrayList<>();
            String pathToFile = pathToFileLabel.getText();
            String text = fileService.readFile(pathToFile);

            String pattern = searchPatternLabel.getText();

            long startKMPAlgorithm = System.nanoTime();
            List<Integer> kmpResults = kmpAlgorithm.runAlgorithm(text, pattern);
            long finishKMPAlgorithm = System.nanoTime();
            long timeKMPAlgorithm = finishKMPAlgorithm - startKMPAlgorithm;

            results.add("\nWyniki algorytmu KMP:");
            results.add(kmpResults.stream()
                    .map(String::valueOf).collect(Collectors.joining("-")));
            results.add("Czas: " + timeKMPAlgorithm + " ns");

            long startIndexOf = System.nanoTime();
            List<Integer> indexOfResults = searchTextJava.checkIndexOfResults(text, pattern);
            long finishIndexOf = System.nanoTime();
            long timeIndexOf = finishIndexOf - startIndexOf;

            results.add("\nWyniki indexOf():");
            results.add(indexOfResults.stream()
                    .map(String::valueOf).collect(Collectors.joining("-")));
            results.add("Czas: " + timeIndexOf + " ns");

            results.add("\nPorównanie wyników:");
            if (kmpResults.equals(indexOfResults)) {
                results.add("ZGODNE");
            } else {
                results.add("NIEZGODNE");
            }

            resultsLabel.setText(results.stream().collect(Collectors.joining("\n")));
        });
    }
}
