package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BookFinder implements BookFinderAPI {

    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        validate(books, tokenizer);
        this.books = books;
        this.tokenizer = tokenizer;
    }

    private void validate(Set<Book> books, TextTokenizer textTokenizer) {
        if (Objects.isNull(books)) {
            throw new IllegalArgumentException("Book set is null.");
        }

        if (Objects.isNull(textTokenizer)) {
            throw new IllegalArgumentException("Text tokenizer is null.");
        }
    }

    @Override
    public Set<Book> allBooks() {
        return Collections.unmodifiableSet(books);
    }

    @Override
    public Set<String> allGenres() {
        return books.stream()
            .map(Book::genres)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        validateAuthorName(authorName);

        return books.stream()
            .filter(book -> book.author().equals(authorName))
            .toList();
    }

    private void validateAuthorName(String authorName) {
        if (Objects.isNull(authorName)) {
            throw new IllegalArgumentException("Author name is null.");
        }

        if (authorName.isEmpty()) {
            throw new IllegalArgumentException("Author name is blank.");
        }
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        validateGenres(genres);
        validateMatchingOption(option);

        if (option.equals(MatchOption.MATCH_ALL)) {
            return searchByGenresAllMatching(genres);
        }

        return searchByGenresAnyMatching(genres);
    }

    private List<Book> searchByGenresAllMatching(Set<String> genres) {
        validateGenres(genres);

        return books.stream()
            .filter(book -> new HashSet<>(book.genres()).containsAll(genres))
            .toList();
    }

    private List<Book> searchByGenresAnyMatching(Set<String> genres) {
        validateGenres(genres);

        return books.stream()
            .filter((book) -> {
                List<String> bookGenres = book.genres();
                for (String genre : genres) {
                    if (bookGenres.contains(genre)) {
                        return true;
                    }
                }
                return false;
            })
            .toList();
    }

    private void validateGenres(Set<String> genres) {
        if (Objects.isNull(genres)) {
            throw new IllegalArgumentException("Genres is null.");
        }
    }

    private void validateMatchingOption(MatchOption option) {
        if (Objects.isNull(option)) {
            throw new IllegalArgumentException("Matching option is null.");
        }
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (Objects.isNull(keywords)) {
            throw new IllegalArgumentException("Keywords is null.");
        }
        validateMatchingOption(option);

        if (option.equals(MatchOption.MATCH_ALL)) {
            return searchByKeywordsAllMatching(keywords);
        }

        return searchByKeywordsAnyMatching(keywords);
    }

    private List<Book> searchByKeywordsAllMatching(Set<String> keywords) {
        return books.stream()
            .filter(book -> new HashSet<>(tokenizer.tokenize(book.title())).containsAll(keywords) ||
                new HashSet<>(tokenizer.tokenize(book.description())).containsAll(keywords))
            .toList();
    }

    private List<Book> searchByKeywordsAnyMatching(Set<String> keywords) {
        return books.stream()
            .filter((book) -> {
                List<String> titleWords = tokenizer.tokenize(book.title());
                List<String> descriptionWords = tokenizer.tokenize(book.description());

                for (String word : keywords) {
                    if (titleWords.contains(word) || descriptionWords.contains(word)) {
                        return true;
                    }
                }
                return false;
            })
            .toList();
    }

}