package bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.blockingqueue.BlockingQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizedinput.TokenizedAnalyzerInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingByConcurrent;

public class TextTokenizer extends Thread {

    private final Set<String> stopwords;
    private final BlockingQueue queue;
    private final AnalyzerInput input;

    public TextTokenizer(Set<String> stopwords, BlockingQueue queue, AnalyzerInput input) {
        validate(stopwords, queue, input);

        this.stopwords = stopwords;
        this.queue = queue;
        this.input = input;
    }

    private void validate(Set<String> stopwords, BlockingQueue queue, AnalyzerInput input) {
        if (Objects.isNull(stopwords)) {
            throw new IllegalArgumentException("Stopwords set is null.");
        }

        if (Objects.isNull(queue)) {
            throw new IllegalArgumentException("Queue is null.");
        }

        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("String input is null.");
        }
    }

    @Override
    public void run() {
        try (var br = new BufferedReader(input.inputReader())) {

            TokenizedAnalyzerInput record = new TokenizedAnalyzerInput(input.inputID(),
                br.lines()
                    .map(line -> line.replaceAll("\\p{Punct}", "").split("\\s+"))
                    .flatMap(Arrays::stream)
                    .parallel()
                    .map(String::toLowerCase)
                    .filter(word -> !stopwords.contains(word))
                    .collect(groupingByConcurrent(word -> word, counting())));

            queue.enqueue(record);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read input.", e);
        }
    }

}
