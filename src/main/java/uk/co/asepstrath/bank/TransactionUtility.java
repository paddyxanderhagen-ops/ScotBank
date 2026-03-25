package uk.co.asepstrath.bank;

import uk.co.asepstrath.bank.model.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionUtility {

    private TransactionUtility() {}

    public static List<Transaction> getExecuted (List<Transaction> all, double currentBalance) {
        List<Transaction> sorted = all.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp)
                        .thenComparing(t -> switch (t.getType()) {
                            case "DEPOSIT" -> 0;
                            case "WITHDRAWAL" -> 1;
                            case "BUY" -> 2;
                            case "SELL" -> 3;
                            default -> 4;
                        }))
                .toList();

        System.out.println(sorted.size());

        for(Transaction t : sorted) {
            System.out.println(t.getId());
        }

        double startingBalance = currentBalance;
        for (Transaction t : sorted) {
            if (t.getType().equals("DEPOSIT") || t.getType().equals("SELL")) {
                startingBalance -= t.getTotalCashAmount();
            } else if (t.getType().equals("WITHDRAWAL") || t.getType().equals("BUY")) {
                if (startingBalance + t.getTotalCashAmount() >= 0) {
                    startingBalance += t.getTotalCashAmount();
                }
            }
        }

        double balance = startingBalance;
        List<Transaction> executed = new ArrayList<>();

        for (Transaction t : sorted) {
            if (t.getType().equals("DEPOSIT") || t.getType().equals("SELL")) {
                balance += t.getTotalCashAmount();
                executed.add(t);
            } else if (t.getType().equals("WITHDRAWAL") || t.getType().equals("BUY")) {
                if (balance - t.getTotalCashAmount() < 0) continue;
                balance -= t.getTotalCashAmount();
                executed.add(t);
            }
        }

        return executed;
    }
}
