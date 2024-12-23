package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookFinderTest {

    @Mock
    private Set<Book> booksMock;

    @Mock
    private TextTokenizer textTokenizerMock;

    @InjectMocks
    private BookFinder bookFinder;

    @Test
    void testUnmodifiableSetOfBook() {
        assertThrows(UnsupportedOperationException.class, () -> bookFinder.allBooks().add(null),
            "Returned book set must be unmodifiable.");
    }

    @Test
    void testReturnsAllGenres() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", " description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        Set<String> result = Set.of("fantasy", "horror", "sci-fi", "drama");

        assertTrue(result.containsAll(bookFinder.allGenres()), "All genres should be correctly extracted in a set.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testGetBooksByAuthor() {
        Book book1 =
            new Book("one", "title1", "hrisi", " description1", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "hrisi", " description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        List<Book> result = List.of(book1, book3);
        String authorName = "hrisi";
        assertTrue(compareListsNoOrderConsideration(result, bookFinder.searchByAuthor(authorName)),
            "Books with same author were not correctly extracted.");

        verify(booksMock, times(1)).stream();
    }

    private boolean compareListsNoOrderConsideration(List<Book> first, List<Book> second) {
        return new HashSet<Book>(first).equals(new HashSet<Book>(second));
    }

    @Test
    void testGetBooksByAuthorNoneMatching() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", " description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        List<Book> result = List.of();
        String authorName = "hrisi";
        assertIterableEquals(result, bookFinder.searchByAuthor(authorName),
            "When no books match the author, an empty set should be returned.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testNullAuthor() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(null),
            "Illegal exception should be thrown when called with null author name.");
    }

    @Test
    void testEmptyAuthor() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByAuthor(""),
            "Illegal exception should be thrown when called with empty author name.");
    }

    @Test
    void testNullGenres() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(null, MatchOption.MATCH_ALL),
            "Illegal exception should be thrown when called with null set of genres.");
    }

    @Test
    void testNullOptionNonNullGenres() {
        Set<String> genres = Set.of();
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(genres, null),
            "Illegal exception should be thrown when called with null matching option.");
    }

    @Test
    void testMatchAllWithMatches() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", " description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        List<Book> result = List.of(book1);

        assertIterableEquals(result, bookFinder.searchByGenres(Set.of("fantasy", "horror"), MatchOption.MATCH_ALL),
            "Expected books which matched all given genres were not found in the result.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAllWithNoMatches() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", " description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        List<Book> result = List.of();

        assertIterableEquals(result, bookFinder.searchByGenres(Set.of("fantasy", "drama"), MatchOption.MATCH_ALL),
            "Empty list is expected when there are no matches.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAnyWithMatches() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", " description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        List<Book> result = List.of(book1, book2, book3);

        assertTrue(compareListsNoOrderConsideration(result,
                bookFinder.searchByGenres(Set.of("fantasy", "drama"), MatchOption.MATCH_ANY)),
            "Expected books which matched some given genres were not found in the result.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAnyNMatches() {
        Book book1 =
            new Book("one", "title1", "author1", " description1", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", " description2", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", " description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        List<Book> result = List.of();

        assertIterableEquals(result, bookFinder.searchByGenres(Set.of("historical"), MatchOption.MATCH_ANY),
            "Empty list is expected when there are no matching genres.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testNullKeywords() {
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByKeywords(null, MatchOption.MATCH_ALL),
            "Illegal exception should be thrown when called with null set of keywords.");
    }

    @Test
    void testNullOptionNonNullKeywords() {
        Set<String> keywords = Set.of();
        assertThrows(IllegalArgumentException.class, () -> bookFinder.searchByGenres(keywords, null),
            "Illegal exception should be thrown when called with null matching option.");
    }

    @Test
    void testMatchAnyWithMatchingKeywordsInDescription() {
        Book book1 =
            new Book("one", "title1", "author1", "description one", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", "description two", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", "description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        when(textTokenizerMock.tokenize("title1")).thenReturn(List.of("title1"));
        when(textTokenizerMock.tokenize("title2")).thenReturn(List.of("title2"));
        when(textTokenizerMock.tokenize("title3")).thenReturn(List.of("title3"));

        when(textTokenizerMock.tokenize("description one")).thenReturn(List.of("description", "one"));
        when(textTokenizerMock.tokenize("description two")).thenReturn(List.of("description", "two"));
        when(textTokenizerMock.tokenize("description3")).thenReturn(List.of("description3"));

        List<Book> result = List.of(book1, book2);

        assertTrue(compareListsNoOrderConsideration(result,
                bookFinder.searchByKeywords(Set.of("description", "title"), MatchOption.MATCH_ANY)),
            "Expected books which matched some keywords were not found in the result.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAnyWithMatchingKeywordsInTitle() {
        Book book1 =
            new Book("one", "title1", "author1", "description one", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", "description two", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", "description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        when(textTokenizerMock.tokenize("title1")).thenReturn(List.of("title1"));
        when(textTokenizerMock.tokenize("title2")).thenReturn(List.of("title2"));
        when(textTokenizerMock.tokenize("title3")).thenReturn(List.of("title3"));

        when(textTokenizerMock.tokenize("description one")).thenReturn(List.of("description", "one"));
        when(textTokenizerMock.tokenize("description two")).thenReturn(List.of("description", "two"));
        when(textTokenizerMock.tokenize("description3")).thenReturn(List.of("description3"));

        List<Book> result = List.of(book1);

        assertTrue(compareListsNoOrderConsideration(result,
                bookFinder.searchByKeywords(Set.of("title1"), MatchOption.MATCH_ANY)),
            "Expected books which matched some keywords were not found in the result.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAnyNoMatchingKeywords() {
        Book book1 =
            new Book("one", "title1", "author1", "description one", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", "description two", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", "description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        when(textTokenizerMock.tokenize("title1")).thenReturn(List.of("title1"));
        when(textTokenizerMock.tokenize("title2")).thenReturn(List.of("title2"));
        when(textTokenizerMock.tokenize("title3")).thenReturn(List.of("title3"));

        when(textTokenizerMock.tokenize("description one")).thenReturn(List.of("one"));
        when(textTokenizerMock.tokenize("description two")).thenReturn(List.of("two"));
        when(textTokenizerMock.tokenize("description3")).thenReturn(List.of("description3"));

        List<Book> result = List.of();

        assertIterableEquals(result, bookFinder.searchByKeywords(Set.of("description", "title"), MatchOption.MATCH_ANY),
            "Empty list is expected when there are no matching keywords");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAllNoMatchingKeywords() {
        Book book1 =
            new Book("one", "title1", "author1", "description one", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", "description two", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", "description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        when(textTokenizerMock.tokenize("title1")).thenReturn(List.of("title1"));
        when(textTokenizerMock.tokenize("title2")).thenReturn(List.of("title2"));
        when(textTokenizerMock.tokenize("title3")).thenReturn(List.of("title3"));

        when(textTokenizerMock.tokenize("description one")).thenReturn(List.of("one"));
        when(textTokenizerMock.tokenize("description two")).thenReturn(List.of("two"));
        when(textTokenizerMock.tokenize("description3")).thenReturn(List.of("description3"));

        List<Book> result = List.of();

        assertIterableEquals(result, bookFinder.searchByKeywords(Set.of("description", "one"), MatchOption.MATCH_ALL),
            "Empty list is expected when there are no matching keywords");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAllWithMatchingKeywordsInTitle() {
        Book book1 =
            new Book("one", "title1", "author1", "description one", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", "description two", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", "description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        when(textTokenizerMock.tokenize("title1")).thenReturn(List.of("title1"));
        when(textTokenizerMock.tokenize("title2")).thenReturn(List.of("title2"));
        when(textTokenizerMock.tokenize("title3")).thenReturn(List.of("title3"));

        when(textTokenizerMock.tokenize("description two")).thenReturn(List.of("description", "two"));
        when(textTokenizerMock.tokenize("description3")).thenReturn(List.of("description3"));

        List<Book> result = List.of(book1);

        assertIterableEquals(result, bookFinder.searchByKeywords(Set.of("title1"), MatchOption.MATCH_ALL),
            "Expected books which matched all keywords were not found in the result.");

        verify(booksMock, times(1)).stream();
    }

    @Test
    void testMatchAllWithMatchingKeywordsInDescription() {
        Book book1 =
            new Book("one", "title1", "author1", "description one", List.of("fantasy", "horror"), 1.0, 1, "url1");
        Book book2 =
            new Book("two", "title2", "author2", "description two", List.of("drama"), 1.0, 1, "url1");
        Book book3 =
            new Book("three", "title3", "author3", "description3", List.of("fantasy", "sci-fi"), 1.0, 1, "url1");

        Set<Book> books = Set.of(book1, book2, book3);
        when(booksMock.stream()).thenReturn(books.stream());

        when(textTokenizerMock.tokenize("title1")).thenReturn(List.of("title1"));
        when(textTokenizerMock.tokenize("title2")).thenReturn(List.of("title2"));
        when(textTokenizerMock.tokenize("title3")).thenReturn(List.of("title3"));

        when(textTokenizerMock.tokenize("description one")).thenReturn(List.of("description", "one"));
        when(textTokenizerMock.tokenize("description two")).thenReturn(List.of("description", "two"));
        when(textTokenizerMock.tokenize("description3")).thenReturn(List.of("description3"));

        List<Book> result = List.of(book1);

        assertIterableEquals(result, bookFinder.searchByKeywords(Set.of("description", "one"), MatchOption.MATCH_ALL),
            "Expected books which matched all keywords were not found in the result.");

        verify(booksMock, times(1)).stream();
    }

}
