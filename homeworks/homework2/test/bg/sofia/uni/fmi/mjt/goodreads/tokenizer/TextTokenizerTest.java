package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextTokenizerTest {

    private TextTokenizer textTokenizer;
    private static final String stopwordsToRead = "a\nabout\nabove\nafter\nagain";
    private String inputText;
    private List<String> result;

    @BeforeEach
    public void setUp() {
        try (Reader stopwordsReader = new StringReader(stopwordsToRead)) {
            textTokenizer = new TextTokenizer(stopwordsReader);
        } catch (IOException e) {
            System.out.println("Tests failed to execute.");
        }
    }

    @Test
    void testThrowsForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> textTokenizer.tokenize(null),
            "Illegal exception should be thrown when tokenizing null.");
    }

    @Test
    void testAllWordsConvertedToLowerCase() {
        inputText = "I love Java";
        result = List.of("i", "love", "java");

        assertIterableEquals(result, textTokenizer.tokenize(inputText),
            "Words from input text should be converted to lower case");
    }

    @Test
    void testRemovePunctuation() {
        inputText = "i don't love java, sorry.";
        result = List.of("i", "dont", "love", "java", "sorry");

        assertIterableEquals(result, textTokenizer.tokenize(inputText),
            "Strings in the result should not contain punctuation.");
    }

    @Test
    void testNoEmptyOrBlankStrings() {
        inputText = "i    love   java";
        result = List.of("i", "love", "java");

        assertIterableEquals(result, textTokenizer.tokenize(inputText),
            "The result should not contain empty or blank strings.");
    }

    @Test
    void testRemovedStopWords() {
        inputText = "i about love a java";
        result = List.of("i", "love", "java");

        assertIterableEquals(result, textTokenizer.tokenize(inputText),
            "The result should not contain stopwords.");
    }

}
