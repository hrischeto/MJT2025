package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BookRecommender implements BookRecommenderAPI {

    private final Set<Book> initialBooks;
    private final SimilarityCalculator calculator;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        if (Objects.isNull(initialBooks)) {
            throw new IllegalArgumentException("Book set is null.");
        }

        if (Objects.isNull(calculator)) {
            throw new IllegalArgumentException("Calculator is null.");
        }

        this.initialBooks = initialBooks;
        this.calculator = calculator;
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        validateBook(origin);
        validateMaxEntries(maxN);

        if (maxN > initialBooks.size()) {
            maxN = initialBooks.size();
        }

        Map<Book, Double> calculatedSimilarity = initialBooks.stream()
            .collect(Collectors.toMap(
                book -> book,
                book -> calculator.calculateSimilarity(origin, book)
            ));

        return calculatedSimilarity.entrySet().stream()
            .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
            .limit(maxN)
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    () -> new TreeMap<>(
                        Comparator.comparing((Book b) -> calculatedSimilarity.get(b)).reversed())));
    }

    private void validateBook(Book book) {
        if (Objects.isNull(book)) {
            throw new IllegalArgumentException("Book is null.");
        }
    }

    private void validateMaxEntries(int maxN) {
        if (maxN <= 0) {
            throw new IllegalArgumentException("Number of entries should be positive");
        }
    }

}