package pl.edu.pbs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {
    public String readFile(String filename) {
        Path filePath = Paths.get("src/pl/edu/pbs/data/" + filename).toAbsolutePath();
        String content = "";
        try {
            content = Files.readAllLines(filePath)
                    .stream()
                    .collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public void writeFile(String filename, List<String> lines) {
        Path filePath = Paths.get("src/pl/edu/pbs/results/" + filename).toAbsolutePath();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
