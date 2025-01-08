package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.io.Reader;
import java.util.Objects;

public record AnalyzerInput(String inputID, Reader inputReader) {
    public AnalyzerInput {
        if (Objects.isNull(inputID)) {
            throw new IllegalArgumentException("Input id is null.");
        }

        if (Objects.isNull(inputReader)) {
            throw new IllegalArgumentException("Input reader is null.");
        }
    }
}