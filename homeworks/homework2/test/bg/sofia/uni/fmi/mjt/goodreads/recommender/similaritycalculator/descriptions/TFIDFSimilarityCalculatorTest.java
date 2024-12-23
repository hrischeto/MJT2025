package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TFIDFSimilarityCalculatorTest {

    @Mock
    private Set<Book> booksMock;

    @Mock
    private TextTokenizer textTokenizerMock;

    @InjectMocks
    private TFIDFSimilarityCalculator calc;

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
    void testTF() {
        Book book =
            new Book("one", "title1", "author1", "academy superhero club superhero", List.of(), 1.0, 1, "url1");

        when(textTokenizerMock.tokenize("academy superhero club superhero")).thenReturn(
            List.of("academy", "superhero", "club", "superhero"));

        Map<String, Double> expected = Map.ofEntries(
            Map.entry("academy", 0.25),
            Map.entry("club", 0.25),
            Map.entry("superhero", 0.5)
        );

        assertEquals(expected, calc.computeTF(book), "TF not correctly calculated");
    }

    @Test
    void testIDF() {
        Book book1 =
            new Book("one", "title1", "author1", "academy superhero club superhero", List.of(), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title1", "author1", "superhero mission save club", List.of(), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title1", "author1", "crime murder mystery club", List.of(), 1.0, 1, "url1");

        Set<Book> bookSet = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenAnswer(invocation -> bookSet.stream());
        when(booksMock.size()).thenReturn(3);

        when(textTokenizerMock.tokenize("academy superhero club superhero")).thenReturn(
            List.of("academy", "superhero", "club", "superhero"));
        when(textTokenizerMock.tokenize("superhero mission save club")).thenReturn(
            List.of("superhero", "mission", "save", "club"));
        when(textTokenizerMock.tokenize("crime murder mystery club")).thenReturn(
            List.of("crime", "murder", "mystery", "club"));

        Map<String, Double> expected = Map.ofEntries(
            Map.entry("academy", Math.log10(3.0 / 1)),
            Map.entry("club", Math.log10(3.0 / 3)),
            Map.entry("superhero", Math.log10(3.0 / 2))
        );

        assertEquals(expected, calc.computeIDF(book1), "IDF not correctly calculated");
    }

    @Test
    void testTFIDF() {

        Book book1 =
            new Book("one", "title1", "author1", "academy superhero club superhero", List.of(), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title1", "author1", "superhero mission save club", List.of(), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title1", "author1", "crime murder mystery club", List.of(), 1.0, 1, "url1");

        Set<Book> bookSet = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenAnswer(invocation -> bookSet.stream());
        when(booksMock.size()).thenReturn(3);

        when(textTokenizerMock.tokenize("academy superhero club superhero")).thenReturn(
            List.of("academy", "superhero", "club", "superhero"));
        when(textTokenizerMock.tokenize("superhero mission save club")).thenReturn(
            List.of("superhero", "mission", "save", "club"));
        when(textTokenizerMock.tokenize("crime murder mystery club")).thenReturn(
            List.of("crime", "murder", "mystery", "club"));

        Map<String, Double> expected = Map.ofEntries(
            Map.entry("academy", 0.25 * Math.log10(3.0 / 1)),
            Map.entry("club", 0.25 * Math.log10(3.0 / 3)),
            Map.entry("superhero", 0.5 * Math.log10(3.0 / 2))
        );

        assertEquals(expected, calc.computeTFIDF(book1), "TF-IDF not correctly calculated");
    }

}
