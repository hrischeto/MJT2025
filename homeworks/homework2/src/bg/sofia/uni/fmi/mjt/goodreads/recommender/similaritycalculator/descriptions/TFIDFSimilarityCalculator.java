package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        if (Objects.isNull(books)) {
            throw new IllegalArgumentException("Book set is null.");
        }
        if (Objects.isNull(tokenizer)) {
            throw new IllegalArgumentException("Tokenizer is null.");
        }

        this.books = books;
        this.tokenizer = tokenizer;
    }

    /*
     * Do not modify!
     */
    @Override
    public double calculateSimilarity(Book first, Book second) {
        validateBook(first);
        validateBook(second);

        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    private void validateBook(Book book) {
        if (Objects.isNull(book)) {
            throw new IllegalArgumentException("Book is null.");
        }
    }

    public Map<String, Double> computeTFIDF(Book book) {
        Map<String, Double> result = new HashMap<>();

        Map<String, Double> tfValues = computeTF(book);
        Map<String, Double> idfValues = computeIDF(book);

        Set<String> words = tfValues.keySet();

        for (String word : words) {
            result.put(word, tfValues.get(word) * idfValues.get(word));
        }

        return result;
    }

    public Map<String, Double> computeTF(Book book) {
        List<String> description = tokenizer.tokenize(book.description());
        Set<String> words = new HashSet<>(description);
        Map<String, Double> result = new HashMap<>();

        for (String word : words) {
            result.put(word, getWordTF(word, description));
        }

        return result;
    }

    private double getWordTF(String calculated, List<String> text) {
        long encountered = text.stream()
            .filter(word -> word.equals(calculated))
            .count();

        return (double) encountered / text.size();
    }

    public Map<String, Double> computeIDF(Book book) {
        Set<String> words = new HashSet<>(tokenizer.tokenize(book.description()));
        Map<String, Double> result = new HashMap<>();

        for (String word : words) {
            result.put(word, getWordIDF(word));
        }

        return result;
    }

    private double getWordIDF(String calculated) {
        long encountered = books.stream()
            .filter(book -> new HashSet<String>(tokenizer.tokenize(book.description()))
                .contains(calculated))
            .count();

        return Math.log10((double) books.size() / encountered);
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
            .mapToDouble(word -> first.get(word) * second.get(word))
            .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
            .map(v -> v * v)
            .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }
}