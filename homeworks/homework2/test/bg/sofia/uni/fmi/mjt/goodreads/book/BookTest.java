package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookTest {
    private final String id = "ID";
    private final String title = "Title";
    private final String author = "Author";
    private final String description = "Description";
    private final String genreString = "[genre1, genre2, genre3]";
    private final String trimmedGenre = genreString.substring(1, genreString.length() - 1);
    private final List<String> genres = Arrays.asList(trimmedGenre.split(", "));
    private final String rating = "1.1";
    private final String ratingCount = "3";
    private final String url = "url";

    private Book book;
    private Book bookFromStaticMethod;

    @BeforeEach
    void setUp() {
        book =
            new Book(id, title, author, description, genres, Double.parseDouble(rating), Integer.parseInt(ratingCount),
                url);

        String[] tokens = {id, title, author, description, genreString, rating, ratingCount, url};

        bookFromStaticMethod = Book.of(tokens);
    }

    @Test
    void testThrowsIfTokensIsNull() {
        assertThrows(IllegalArgumentException.class, () -> Book.of(null),
            "When given null argument IllegalArgumentException should be thrown.");
    }

    @Test
    void testSetTitle() {
        assertEquals(book.title(), bookFromStaticMethod.title(),
            "Title should be correctly set with static factory method.");
    }

    @Test
    void testSetAuthor() {
        assertEquals(book.author(), bookFromStaticMethod.author(),
            "Author should be correctly set with static factory method.");
    }

    @Test
    void testSetDescription() {
        assertEquals(book.description(), bookFromStaticMethod.description(),
            "Description should be correctly set with static factory method.");
    }

    @Test
    void testSetGenres() {
        assertEquals(book.genres(), bookFromStaticMethod.genres(),
            "Genres should be correctly set with static factory method.");
    }

    @Test
    void testSetRating() {
        assertEquals(book.rating(), bookFromStaticMethod.rating(),
            "Rating should be correctly set with static factory method.");
    }

    @Test
    void testSetRatingCount() {
        assertEquals(book.ratingCount(), bookFromStaticMethod.ratingCount(),
            "Rating count should be correctly set with static factory method.");
    }

    @Test
    void testSetURL() {
        assertEquals(book.URL(), bookFromStaticMethod.URL(),
            "URL should be correctly set with static factory method.");
    }
}
