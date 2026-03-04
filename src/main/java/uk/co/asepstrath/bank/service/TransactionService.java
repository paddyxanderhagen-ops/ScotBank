package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;

import java.util.HashMap;
import java.util.List;

public class TransactionService {
    public String getById (String id, List<Transaction> transactionList ) {
        for (Transaction transaction : transactionList) {
            if (transaction.getId().equals(id)) {
                return "id: " + transaction.getId() + "\ntimestamp: " + transaction.getTimestamp() + "\ntype: "  + transaction.getType() + "\ninvestorId: " + transaction.getInvestorId() + "\nticker: " + transaction.getTicker() + "\ntotalCashAmount: " + transaction.getTotalCashAmount() + "\n\n";
            }
        }
        return "Transaction:" + id + " not found";
    }

    public HashMap<String, Integer> getNumberOfShares(String InvestorId, List<Transaction> transactionList) {
        HashMap<String, Integer> shares = new HashMap<>();
        int c;
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equals("BUY")) {
                if (transaction.getInvestorId().equals(InvestorId)) {
                    c = shares.getOrDefault(transaction.getTicker(), 0);
                    if (c == 0) {
                        shares.put(transaction.getTicker(), 1);
                    } else {
                        shares.put(transaction.getTicker(), c + 1);
                    }
                }
            }
        }
        return shares;
    }
}
