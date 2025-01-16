package bg.sofia.uni.fmi.mjt.poll.command.factory;

import bg.sofia.uni.fmi.mjt.poll.command.Command;
import bg.sofia.uni.fmi.mjt.poll.command.commands.BadCommand;
import bg.sofia.uni.fmi.mjt.poll.command.commands.CreatePoll;
import bg.sofia.uni.fmi.mjt.poll.command.commands.ListPolls;
import bg.sofia.uni.fmi.mjt.poll.command.commands.SubmitVote;

import java.util.Arrays;
import java.util.List;

public class CommandFactory {

    private static final String CREATE_POLL = "create-poll";
    private static final String LIST_POLLS = "list-polls";
    private static final String SUBMIT_VOTE = "submit-vote";

    private static final String SEPARATOR = "\\s+";

    public static Command newCommand(String clientInput) {
        List<String> tokens = Arrays.stream(clientInput.split(SEPARATOR))
            .map(String::strip)
            .toList();

        String[] args = tokens.subList(1, tokens.size()).toArray(new String[0]);

        String commandTitle = tokens.getFirst();
        return switch (commandTitle) {
            case CREATE_POLL -> new CreatePoll(args);
            case LIST_POLLS -> new ListPolls();
            case SUBMIT_VOTE -> new SubmitVote(args);
            default -> new BadCommand();
        };
    }

}
