package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookRecommenderTest {

    private static final double DELTA = 0.000001;

    @Mock
    private Set<Book> booksMock;

    @Mock
    private SimilarityCalculator calculatorMock;

    @InjectMocks
    private BookRecommender bookRecommender;

    @Test
    void testNullOriginBook() {
        assertThrows(IllegalArgumentException.class, () -> bookRecommender.recommendBooks(null, 1));
    }

    @Test
    void testNegativeMaxEntries() {
        Book bookMock = mock(Book.class);

        assertThrows(IllegalArgumentException.class, () -> bookRecommender.recommendBooks(bookMock, -1));
    }

    @Test
    void testZeroMaxEntries() {
        Book bookMock = mock(Book.class);

        assertThrows(IllegalArgumentException.class, () -> bookRecommender.recommendBooks(bookMock, 0));
    }

    @Test
    void testRecommendSimilarBooks() {
        Book origin = mock(Book.class);

        Book book1 = mock(Book.class);
        Book book2 = mock(Book.class);
        Book book3 = mock(Book.class);

        Set<Book> bookSet = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenAnswer(invocation -> bookSet.stream());
        when(booksMock.size()).thenReturn(3);

        when(calculatorMock.calculateSimilarity(origin, book1)).thenReturn(0.9);
        when(calculatorMock.calculateSimilarity(origin, book2)).thenReturn(0.6);
        when(calculatorMock.calculateSimilarity(origin, book3)).thenReturn(0.3);

        SortedMap<Book, Double> expected = new TreeMap<>(
            (b1, b2) -> {
                return Double.compare(
                    calculatorMock.calculateSimilarity(origin, b2),
                    calculatorMock.calculateSimilarity(origin, b1));
            }
        );
        expected.put(book1, 0.9);
        expected.put(book2, 0.6);

        assertEquals(expected, bookRecommender.recommendBooks(origin, 2), "Similar books not correctly evaluated.");
    }

    @Test
    void testMaxNBiggerThatSize() {
        Book origin = mock(Book.class);

        Book book1 = mock(Book.class);
        Book book2 = mock(Book.class);
        Book book3 = mock(Book.class);

        Set<Book> bookSet = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenAnswer(invocation -> bookSet.stream());
        when(booksMock.size()).thenReturn(3);

        when(calculatorMock.calculateSimilarity(origin, book1)).thenReturn(0.9);
        when(calculatorMock.calculateSimilarity(origin, book2)).thenReturn(0.6);
        when(calculatorMock.calculateSimilarity(origin, book3)).thenReturn(0.3);

        assertEquals(3, bookRecommender.recommendBooks(origin, 4).size(),
            "When maxN is more that the size of the book set, expected size of result is book set size.");
    }

}
