package bg.sofia.uni.fmi.mjt.sentimentanalyzer.scorecalculator;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.blockingqueue.BlockingQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizedinput.TokenizedAnalyzerInput;

import java.util.Map;
import java.util.Objects;

public class ScoreCalculator extends Thread {

    private final BlockingQueue queue;
    private final Map<String, SentimentScore> sentimentLexicon;

    private final Map<String, SentimentScore> sentimentScoreMap;

    public ScoreCalculator(BlockingQueue queue, Map<String, SentimentScore> sentimentLexicon,
                           Map<String, SentimentScore> sentimentScoreMap) {
        validate(queue);
        validate(sentimentLexicon);
        validate(sentimentScoreMap);

        this.queue = queue;
        this.sentimentLexicon = sentimentLexicon;
        this.sentimentScoreMap = sentimentScoreMap;
    }

    private void validate(Object obj) {
        if (Objects.isNull(obj)) {
            throw new IllegalArgumentException("Null argument.");
        }
    }

    @Override
    public void run() {
        while (true) {
            TokenizedAnalyzerInput toAnalyse;
            synchronized (queue) {
                if (queue.canDequeue())
                    toAnalyse = queue.dequeue();
                else {
                    break;
                }
            }

            long score = toAnalyse.text().keySet().stream()
                .mapToLong(key -> {
                    if (sentimentLexicon.containsKey(key)) {
                        return sentimentLexicon.get(key).getScore() * toAnalyse.text().get(key);
                    } else {
                        return 0;
                    }
                })
                .sum();

            synchronized (sentimentScoreMap) {
                sentimentScoreMap.put(toAnalyse.id(), SentimentScore.fromScore((int) score));
            }
        }
    }
}
