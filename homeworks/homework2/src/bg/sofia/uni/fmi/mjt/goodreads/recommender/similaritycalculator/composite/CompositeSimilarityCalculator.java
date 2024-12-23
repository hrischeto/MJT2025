package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Map;
import java.util.Objects;

public class CompositeSimilarityCalculator implements SimilarityCalculator {

    private final Map<SimilarityCalculator, Double> similarityCalculatorMap;

    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        if (Objects.isNull(similarityCalculatorMap)) {
            throw new IllegalArgumentException("Map of composed calculators is null.");
        }

        this.similarityCalculatorMap = similarityCalculatorMap;
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        validateBook(first);
        validateBook(second);

        return similarityCalculatorMap.keySet().stream()
            .mapToDouble(calc -> calc.calculateSimilarity(first, second) * similarityCalculatorMap.get(calc))
            .sum();
    }

    private void validateBook(Book book) {
        if (Objects.isNull(book)) {
            throw new IllegalArgumentException("Book is null.");
        }
    }
}