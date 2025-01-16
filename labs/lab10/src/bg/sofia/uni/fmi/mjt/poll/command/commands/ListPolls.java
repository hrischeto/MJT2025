package bg.sofia.uni.fmi.mjt.poll.command.commands;

import bg.sofia.uni.fmi.mjt.poll.command.Command;
import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.Map;
import java.util.Set;

public class ListPolls implements Command {

    @Override
    public String execute(PollRepository pollRepository) {
        try {
            Map<Integer, Poll> polls = pollRepository.getAllPolls();
            if (polls.isEmpty()) {
                return "\"status\":\"ERROR\", message: No active polls available.";
            }

            StringBuilder response = new StringBuilder(String.format("\"status\":\"OK\", polls: %s", polls.size()));

            Set<Integer> keys = polls.keySet();
            for (int key : keys) {
                Poll current = polls.get(key);
                response.append(String.format(" {question: \"%s\", options: ", current.question()));

                Set<String> options = current.options().keySet();
                for (String opt : options) {
                    response.append(opt).append(": ").append(current.options().get(opt)).append(", ");
                }
                response.deleteCharAt(response.lastIndexOf(", "));
                response.append("}");
            }

            return response.toString();
        } catch (RuntimeException e) {
            return "\"status\":\"ERROR\", message: Unknown error occurred";
        }
    }
}
