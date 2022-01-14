package pl.edu.pbs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlgorithmResult {
    private String filename;
    private List<String> lines = new ArrayList<>();

    public AlgorithmResult(String filename) {
        this.filename = filename;
    }

    public void addHeader(List<String> patterns) {
        String line = patterns.stream()
                .map(pattern -> String.valueOf(pattern.length()))
                .collect(Collectors.joining(";"));

        lines.add(line);
    }

    public void addLine(List<Long> times) {
        String line = times.stream()
                .map(time -> String.valueOf(time))
                .collect(Collectors.joining(";"));

        lines.add(line);
    }

    public String getFilename() {
        return filename;
    }

    public List<String> getLines() {
        return lines;
    }
}
