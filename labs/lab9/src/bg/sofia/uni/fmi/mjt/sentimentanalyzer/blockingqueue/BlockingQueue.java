package bg.sofia.uni.fmi.mjt.sentimentanalyzer.blockingqueue;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.tokenizedinput.TokenizedAnalyzerInput;

import java.util.ArrayList;
import java.util.List;

public class BlockingQueue {

    private final List<TokenizedAnalyzerInput> queue;
    private int poppedCount = 0;
    private final int maxPopped;

    public BlockingQueue(int maxPopped) {
        if (maxPopped <= 0) {
            throw new IllegalArgumentException("MaxPopped should be positive.");
        }
        this.maxPopped = maxPopped;
        queue = new ArrayList<>();
    }

    public synchronized void enqueue(TokenizedAnalyzerInput task) {
        queue.add(task);

        if (queue.size() == 1) {
            notifyAll();
        }
    }

    public synchronized TokenizedAnalyzerInput dequeue() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new SentimentAnalysisException("Error while retrieving task from queue", e);
            }
        }

        poppedCount++;

        return queue.removeFirst();
    }

    public synchronized boolean canDequeue() {
        return poppedCount < maxPopped;
    }

}
