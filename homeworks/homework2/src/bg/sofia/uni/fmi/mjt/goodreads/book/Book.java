package bg.sofia.uni.fmi.mjt.goodreads.book;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Book(
    String ID,
    String title,
    String author,
    String description,
    List<String> genres,
    double rating,
    int ratingCount,
    String URL
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(ID, book.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

    public static Book of(String[] tokens) {
        if (Objects.isNull(tokens)) {
            throw new IllegalArgumentException("Tokens is null.");
        }

        return new Book(tokens[BookParsingConstants.ID_INDEX], tokens[BookParsingConstants.TITLE_INDEX],
            tokens[BookParsingConstants.AUTHOR_INDEX],
            tokens[BookParsingConstants.DESCRIPTION_INDEX],
            getGenresFromString(tokens[BookParsingConstants.GENRES_INDEX]),
            Double.parseDouble(tokens[BookParsingConstants.RATING_INDEX]),
            getRatingCount(tokens[BookParsingConstants.RATING_COUNT_INDEX]), tokens[BookParsingConstants.URL_INDEX]);
    }

    private static List<String> getGenresFromString(String genres) {
        String trimmed = genres.substring(1, genres.length() - 1);
        return Arrays.asList(trimmed.split(BookParsingConstants.VALUE_SEPARATOR));
    }

    private static int getRatingCount(String ratingCount) {
        return Integer.parseInt(ratingCount.replace(BookParsingConstants.INT_SEPARATOR, ""));
    }
}