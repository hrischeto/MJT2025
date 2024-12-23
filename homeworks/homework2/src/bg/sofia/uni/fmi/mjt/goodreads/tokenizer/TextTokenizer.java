package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TextTokenizer {

    private final Set<String> stopwords;

    public TextTokenizer(Reader stopwordsReader) {
        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("Input is null");
        }

        String[] tokens = input.replaceAll("\\p{Punct}", "").split("\\s+");

        return Arrays.stream(tokens)
            .map(String::toLowerCase)
            .filter(word->!stopwords.contains(word))
            .toList();
    }

    public Set<String> stopwords() {
        return stopwords;
    }

}