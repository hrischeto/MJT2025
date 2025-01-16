package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryPollRepository implements PollRepository {

    private final Map<Integer, Poll> polls;

    public InMemoryPollRepository() {
        polls = new HashMap<>();
    }

    @Override
    public int addPoll(Poll poll) {
        if (Objects.isNull(poll)) {
            throw new IllegalArgumentException("Null poll.");
        }

        int id = polls.size();
        polls.put(id, poll);
        return id;
    }

    @Override
    public Poll getPoll(int pollId) {
        return polls.getOrDefault(pollId, null);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return Collections.unmodifiableMap(polls);
    }

    @Override
    public void clearAllPolls() {
        polls.clear();
    }
}
