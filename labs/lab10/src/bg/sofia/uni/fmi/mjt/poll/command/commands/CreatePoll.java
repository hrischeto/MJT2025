package bg.sofia.uni.fmi.mjt.poll.command.commands;

import bg.sofia.uni.fmi.mjt.poll.command.Command;
import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreatePoll implements Command {

    private static final int MINIMUM_INPUT_LENGTH = 3;

    private final String question;
    private final List<String> options;

    public CreatePoll(String... input) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("Null input to \"create poll\" command.");
        }

        if (input.length < MINIMUM_INPUT_LENGTH) {
            question = null;
            options = null;
        } else {
            question = input[0];
            options = Arrays.asList(input).subList(1, input.length);
        }
    }

    @Override
    public String execute(PollRepository pollRepository) {
        if (Objects.isNull(question) || Objects.isNull(options)) {
            return "\"status\":\"ERROR\", message: Could not create a poll due to invalid input.";
        }

        try {
            Map<String, Integer> optionMap = new HashMap<>();

            for (String option : options) {
                optionMap.put(option, 0);
            }

            int id = pollRepository.addPoll(new Poll(question, optionMap));

            return String.format("\"status\":\"OK\", message:Poll %s created successfully", id);
        } catch (RuntimeException e) {
            return "\"status\":\"ERROR\", message: Unknown error occurred";
        }
    }
}
