package pl.edu.pbs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
}
