package pl.edu.pbs.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.pbs.algorithm.KMPAlgorithm;
import pl.edu.pbs.algorithm.SearchTextJava;
import pl.edu.pbs.service.CharCounter;
import pl.edu.pbs.service.FileService;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Controller {
    @FXML
    private TextField pathToFileLabel;
    @FXML
    private TextField searchPatternLabel;
    @FXML
    private TextArea kmpResultsText;
    @FXML
    private TextArea indexOfResultsText;
    @FXML
    private TextField kmpTimeField;
    @FXML
    private TextField indexOfTimeField;
    @FXML
    private Button searchBtn;
    @FXML
    private ImageView fileChooserBtn;
    @FXML
    private BarChart<String, Integer> chart;

    private FileService fileService;
    private KMPAlgorithm kmpAlgorithm;
    private SearchTextJava searchTextJava;
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
                String pathToFile = selectedFile.getAbsolutePath();
                pathToFileLabel.setText(pathToFile);
                Map<Character, Integer> data = CharCounter.analyzeText(fileService.readFile(pathToFile));
                drawChart(data);
                clearTextFields();
            }
        });
    }

    public void initSearchBtn() {
        searchBtn.setOnAction(actionEvent -> {
            String pathToFile = pathToFileLabel.getText();
            String pattern = searchPatternLabel.getText();

            if (pathToFile.isEmpty() || pattern.isEmpty()) {
                displayErrorAlert();
            } else {
                String text = fileService.readFile(pathToFile);

                long startKMPAlgorithm = System.nanoTime();
                List<Integer> kmpResults = kmpAlgorithm.runAlgorithm(text, pattern);
                long finishKMPAlgorithm = System.nanoTime();
                double timeKMPAlgorithm = (finishKMPAlgorithm - startKMPAlgorithm) / 1000000.0;
                kmpTimeField.setText(timeKMPAlgorithm + " ms");
                if (kmpResults.size() > 0) {
                    kmpResultsText.setText("Znalezione pozycje: \n");
                    kmpResults.stream().map(String::valueOf).forEach(result -> kmpResultsText.appendText(result + "\n"));
                } else {
                    kmpResultsText.setText("Nie znaleziono żadnych wystąpień wzorca!");
                }

                long startIndexOf = System.nanoTime();
                List<Integer> indexOfResults = searchTextJava.checkIndexOfResults(text, pattern);
                long finishIndexOf = System.nanoTime();
                double timeIndexOf = (finishIndexOf - startIndexOf) / 1000000.0;
                indexOfTimeField.setText(timeIndexOf + " ms");
                if (indexOfResults.size() > 0) {
                    indexOfResultsText.setText("Znalezione pozycje: \n");
                    indexOfResults.stream().map(String::valueOf).forEach(result -> indexOfResultsText.appendText(result + "\n"));
                } else {
                    indexOfResultsText.setText("Nie znaleziono żadnych wystąpień wzorca!");
                }

                if (kmpResults.size() > 0 || indexOfResults.size() > 0) {
                    kmpResultsText.appendText("\nPorównanie wyników:\n");
                    indexOfResultsText.appendText("\nPorównanie wyników:\n");
                    if (kmpResults.equals(indexOfResults)) {
                        kmpResultsText.appendText("ZGODNE");
                        indexOfResultsText.appendText("ZGODNE");
                    } else {
                        kmpResultsText.appendText("NIEZGODNE");
                        indexOfResultsText.appendText("NIEZGODNE");
                    }
                }
            }
        });
    }

    private void displayErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Brak danych");
        alert.setContentText("Aby uruchomić algorytm, podaj ścieżkę do pliku oraz szukany wzorzec!");
        alert.showAndWait();
    }

    private void drawChart(Map<Character, Integer> data) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        chart.setTitle("Rozkład statystyczny znaków w tekście");
        xAxis.setLabel("Znak");
        yAxis.setLabel("Liczba wystąpień");
        // TODO: nie wyświetlają się etykiety osi

        chart.getData().remove(0, chart.getData().size());
        for (Character character : data.keySet()) {
            Integer count = data.get(character);
            XYChart.Series series = new XYChart.Series(character.toString(),
                    FXCollections.observableArrayList(new XYChart.Data<>("", count)));
            chart.getData().add(series);
        }
    }

    private void clearTextFields() {
        kmpResultsText.clear();
        indexOfResultsText.clear();
        kmpTimeField.clear();
        indexOfTimeField.clear();
    }
}
