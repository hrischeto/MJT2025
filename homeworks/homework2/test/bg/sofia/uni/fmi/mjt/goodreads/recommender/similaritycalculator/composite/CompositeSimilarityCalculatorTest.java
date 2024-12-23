package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompositeSimilarityCalculatorTest {

    private static final double DELTA = 0.000001;

    @Mock
    private Map<SimilarityCalculator, Double> composedMock;

    @InjectMocks
    private CompositeSimilarityCalculator calc;

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
    void testSimilarity() {
        Book book1 =
            new Book("one", "title1", "author1", "academy superhero club superhero", List.of(), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title1", "author1", "superhero mission save club", List.of(), 1.0, 1, "url1");

        SimilarityCalculator calc1 = mock(SimilarityCalculator.class);
        SimilarityCalculator calc2 = mock(SimilarityCalculator.class);

        when(calc1.calculateSimilarity(book1, book2)).thenReturn(0.5);
        when(calc2.calculateSimilarity(book1, book2)).thenReturn(0.5);

        when(composedMock.keySet()).thenReturn(Set.of(calc1, calc2));
        when(composedMock.get(calc1)).thenReturn(0.2);
        when(composedMock.get(calc2)).thenReturn(0.3);

        double result = 0.5 * 0.2 + 0.5 * 0.3;

        assertEquals(result, calc.calculateSimilarity(book1, book2), DELTA, "Similarity not correctly calculated.");

        verify(composedMock, times(1)).keySet();

        verify(composedMock, times(1)).get(calc1);
        verify(composedMock, times(1)).get(calc2);
    }

}
