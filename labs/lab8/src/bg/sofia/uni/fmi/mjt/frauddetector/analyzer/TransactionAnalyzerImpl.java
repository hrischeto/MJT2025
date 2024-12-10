package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private final List<Transaction> transactions;
    private final List<Rule> rules;
    private final double EPSILON = 0.000001;
    private final double MAX_SUMMED_RULE_WEIGHT = 1.0;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {

        var br = new BufferedReader(reader);
        transactions = br.lines().skip(1).map(Transaction::of).toList();

        double summedRuleWeight = rules.parallelStream()
            .mapToDouble(Rule::weight)
            .sum();

        if (Math.abs(summedRuleWeight - MAX_SUMMED_RULE_WEIGHT) > EPSILON) {
            throw new IllegalArgumentException("Invalid ruleset.");
        } else {
            this.rules = rules;
        }

    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
            .map(Transaction::accountID)
            .distinct().
            toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream().collect(Collectors.groupingBy(Transaction::channel,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
    }

    /**
     * Calculates the total amount spent by a specific user, identified by their account ID.
     *
     * @param accountID the account ID for which the total amount spent is calculated.
     * @return the total amount spent by the user as a double.
     * @throws IllegalArgumentException if the accountID is null or empty.
     */
    double amountSpentByUser(String accountID);

    /**
     * Retrieves all transactions associated with a specific account ID.
     *
     * @param accountId the account ID for which transactions are retrieved.
     * @return a list of Transaction objects associated with the specified account.
     * @throws IllegalArgumentException if the account ID is null or empty.
     */
    List<Transaction> allTransactionsByUser(String accountId);

    /**
     * Returns the risk rating of an account with the specified ID.
     *
     * @return the risk rating as a double-precision floating-point number in the interval [0.0, 1.0].
     * @throws IllegalArgumentException if the account ID is null or empty.
     */
    double accountRating(String accountId);

    /**
     * Calculates the risk score for each account based on the loaded rules.
     * The score for each account is a double-precision floating-point number in the interval [0, 1] and is
     * formed by summing up the weights of all applicable rules to the account transactions.
     * The result is sorted in descending order, with the highest-risk accounts listed first.
     *
     * @return a map where keys are account IDs (strings) and values are risk scores (doubles).
     */
    SortedMap<String, Double> accountsRisk();
}
