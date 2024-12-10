package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

public enum Channel {
    ATM, ONLINE, BRANCH;

    public static Channel parse(String channel) {

        return switch (channel) {

            case "ATM" -> Channel.ATM;
            case "ONLINE" -> Channel.ONLINE;
            case "BRANCH" -> Channel.BRANCH;

            default -> throw new IllegalArgumentException("No such channel.");

        };

    }

}