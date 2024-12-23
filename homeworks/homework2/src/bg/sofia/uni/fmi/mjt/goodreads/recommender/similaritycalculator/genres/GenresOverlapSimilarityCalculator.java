package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        validateBook(first);
        validateBook(second);

        Set<String> firstGenreSet = new HashSet<>(first.genres());
        Set<String> secondGenreSet = new HashSet<>(second.genres());

        int minSize = Math.min(firstGenreSet.size(), secondGenreSet.size());
        if (minSize == 0) {
            return 0.0;
        }

        firstGenreSet.retainAll(secondGenreSet);

        return ((double) firstGenreSet.size()) / minSize;
    }

    private void validateBook(Book book) {
        if (Objects.isNull(book)) {
            throw new IllegalArgumentException("Book is null.");
        }
    }

}