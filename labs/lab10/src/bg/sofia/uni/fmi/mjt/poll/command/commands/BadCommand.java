package bg.sofia.uni.fmi.mjt.poll.command.commands;

import bg.sofia.uni.fmi.mjt.poll.command.Command;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

public class BadCommand implements Command {

    static final String BAD_COMMAND_MESSAGE = "\"status\":\"ERROR\", message: Invalid command string.";

    @Override
    public String execute(PollRepository pollRepository) {
        return BAD_COMMAND_MESSAGE;
    }

}
