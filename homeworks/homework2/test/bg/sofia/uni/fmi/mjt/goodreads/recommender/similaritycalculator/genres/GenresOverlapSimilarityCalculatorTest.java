package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class GenresOverlapSimilarityCalculatorTest {

    private static final double DELTA = 0.000001;

    GenresOverlapSimilarityCalculator calc = new GenresOverlapSimilarityCalculator();

    @Test
    void testFirstNullBook() {
        Book book = mock(Book.class);

        assertThrows(IllegalArgumentException.class, () -> calc.calculateSimilarity(null, book),
            "IllegalArgumentException should be thrown when a book is null.");
    }

    @Test
    void testSecondNullBook() {
        Book book = mock(Book.class);

        assertThrows(IllegalArgumentException.class, () -> calc.calculateSimilarity(book, null),
            "IllegalArgumentException should be thrown when a book is null.");
    }

    @Test
    void testBookWithZeroGenres() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of(), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");

        assertEquals(0.0, calc.calculateSimilarity(book1, book2), DELTA,
            "When a book has an empty list of genres, the similarity should be zero.");
    }

    @Test
    void testCompleteSimilarity() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("drama"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");

        assertEquals(1.0, calc.calculateSimilarity(book1, book2), DELTA,
            "When books have the same genres, the similarity should be 1");
    }

    @Test
    void testNoSimilarity() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");

        assertEquals(0.0, calc.calculateSimilarity(book1, book2), DELTA,
            "When books do not have any genres in common, the similarity should be 0");
    }

    @Test
    void testSomeSimilarity() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("drama", " horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama", "comedy"), 1.0, 1, "url1");

        assertEquals(0.5, calc.calculateSimilarity(book1, book2), DELTA,
            "Similarity not calculated properly for books with some genres in common.");
    }

}
