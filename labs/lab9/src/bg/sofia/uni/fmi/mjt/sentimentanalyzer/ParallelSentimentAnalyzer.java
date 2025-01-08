package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.blockingqueue.BlockingQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.scorecalculator.ScoreCalculator;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizer.TextTokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {

    private final int workersCount;

    private final Set<String> stopWords;
    private final Map<String, SentimentScore> sentimentLexicon;

    private BlockingQueue queue;

    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        validateArguments(workersCount, stopWords, sentimentLexicon);

        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
    }

    private void validateArguments(int workersCount, Set<String> stopWords,
                                   Map<String, SentimentScore> sentimentLexicon) {
        if (workersCount <= 0) {
            throw new IllegalArgumentException("Worker threads count should be positive.");
        }

        if (Objects.isNull(stopWords)) {
            throw new IllegalArgumentException("Stopwords set is null.");
        }

        if (Objects.isNull(sentimentLexicon)) {
            throw new IllegalArgumentException("Sentiment lexicon is null.");
        }
    }

    private void startProducers(AnalyzerInput... input) {
        TextTokenizer[] producers = new TextTokenizer[input.length];

        for (int i = 0; i < producers.length; i++) {
            producers[i] = new TextTokenizer(stopWords, queue, input[i]);
            producers[i].start();
        }
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) {
        this.queue = new BlockingQueue(input.length);

        startProducers(input);

        Map<String, SentimentScore> result = new HashMap<>();
        ScoreCalculator[] calculators = new ScoreCalculator[workersCount];
        for (int i = 0; i < workersCount; i++) {
            calculators[i] = new ScoreCalculator(queue, sentimentLexicon, result);
            calculators[i].start();
        }

        for (int i = 0; i < workersCount; i++) {
            try {
                calculators[i].join();
            } catch (InterruptedException e) {
                throw new SentimentAnalysisException("Error when calculating sentiment score.", e);
            }
        }

        return result;
    }

}
