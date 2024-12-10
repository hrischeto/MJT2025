package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    private static final String TRANSACTION_ATTRIBUTE_DELIMITER = ",";
    private static final String DATE_TIME_PARSING_PATTERN="uuuu MM dd HH mm ss";
    private static final DateTimeFormatter formatter=DateTimeFormatter.ofPattern(DATE_TIME_PARSING_PATTERN);

    public static Transaction of(String line) {

        final String[] tokens = line.split(TRANSACTION_ATTRIBUTE_DELIMITER);

        return new Transaction(tokens[0], tokens[1], Double.parseDouble(tokens[2]),
            LocalDateTime.parse(tokens[3], formatter), tokens[4], Channel.parse(tokens[5]));
    }

}
