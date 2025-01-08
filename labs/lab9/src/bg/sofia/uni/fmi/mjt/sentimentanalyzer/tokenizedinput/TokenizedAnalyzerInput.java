package bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizedinput;

import java.util.Map;

public record TokenizedAnalyzerInput(String id, Map<String, Long> text) {
}
