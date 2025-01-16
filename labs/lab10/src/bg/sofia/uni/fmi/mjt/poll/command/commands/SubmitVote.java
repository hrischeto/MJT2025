package bg.sofia.uni.fmi.mjt.poll.command.commands;

import bg.sofia.uni.fmi.mjt.poll.command.Command;
import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.Objects;

public class SubmitVote implements Command {

    private static final int EXPECTED_ARGUMENTS = 2;
    private int pollId;
    private String optionVote;
    private final String[] input;

    public SubmitVote(String... input) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("Invalid input in \"submit vote\" command.");
        }

        this.input = input;
    }

    @Override
    public String execute(PollRepository pollRepository) {
        try {
            String validate = validateInput();
            if (Objects.nonNull(validate)) {
                return validate;
            }

            optionVote = input[1];
            Poll poll = pollRepository.getPoll(pollId);
            if (Objects.isNull(poll)) {
                return String.format("\"status\":\"ERROR\", message: No poll with id %s", pollId);
            }

            int currentCount = poll.options().getOrDefault(optionVote, -1);
            if (currentCount == -1) {
                return String.format("\"status\":\"ERROR\", message: No option %s in poll %s.", optionVote, pollId);
            }

            poll.options().put(optionVote, ++currentCount);
            return String.format("\"status\":\"OK\", message: Vote submitted successfully for poll %s and option %s",
                pollId, optionVote);
        } catch (RuntimeException e) {
            return "\"status\":\"ERROR\", message: Unknown error occurred";
        }
    }

    private String validateInput() {
        if (input.length < EXPECTED_ARGUMENTS) {
            return "\"status\":\"ERROR\", " +
                "message: Invalid ID provided for command \"submit-vote\": only integer values are allowed";
        }

        try {
            pollId = Integer.parseInt(input[0]);
        } catch (NumberFormatException e) {
            return "\"status\":\"ERROR\", " +
                "message: Invalid ID provided for command \"submit-vote\": only integer values are allowed";
        }

        return null;
    }
}
