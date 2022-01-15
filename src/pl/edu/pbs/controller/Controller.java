package pl.edu.pbs.controller;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.pbs.algorithm.KMPAlgorithm;
import pl.edu.pbs.algorithm.SearchTextJava;
import pl.edu.pbs.service.FileService;

import java.io.File;
import java.nio.file.Paths;
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
    @FXML
    private ImageView fileChooserBtn;

    private FileService fileService;
    private KMPAlgorithm kmpAlgorithm;
    private SearchTextJava searchTextJava;
    private List<String> results;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        this.fileService = new FileService();
        this.kmpAlgorithm = new KMPAlgorithm();
        this.searchTextJava = new SearchTextJava();
        initSearchBtn();
        initFileChooserBtn();
    }

    private void initFileChooserBtn() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        String dataPath = Paths.get("src/pl/edu/pbs/data").toAbsolutePath().toString();
        fileChooser.setInitialDirectory(new File(dataPath));
        fileChooserBtn.setCursor(Cursor.HAND);
        fileChooserBtn.setOnMousePressed(mouseEvent -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                pathToFileLabel.setText(selectedFile.getAbsolutePath());
            }
        });
    }

    public void initSearchBtn() {
        searchBtn.setOnAction(actionEvent -> {
            results = new ArrayList<>();
            String pathToFile = pathToFileLabel.getText();
            String pattern = searchPatternLabel.getText();

            if (pathToFile.isEmpty() || pattern.isEmpty()) {
                resultsLabel.setText("Aby uruchomić algorytm podaj ścieżkę do pliku oraz szukany wzorzec.");
            } else {
                String text = fileService.readFile(pathToFile);

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
            }
        });
    }
}
