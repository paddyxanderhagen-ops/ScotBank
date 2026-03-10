package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.controller.PricesController;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;

import java.math.BigDecimal;
import java.math.MathContext;
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

    public HashMap<String, BigDecimal> getNumberOfShares(String InvestorId, List<Transaction> transactionList) {
        HashMap<String, BigDecimal> shares = new HashMap<>();
        BigDecimal c;
        BigDecimal d;
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equals("BUY")) {
                if (transaction.getInvestorId().equals(InvestorId)) {
                    c = shares.getOrDefault(transaction.getTicker(), BigDecimal.ZERO);
                    if (c.equals(0)) {
                        PricesController pricesController = new PricesController();
                        d = transaction.getTotalCashAmount().divide(pricesController.Price(transaction.getTicker(), transaction.getTimestamp()), 4);
                        shares.put(transaction.getTicker(), d);
                    } else {
                        PricesController pricesController = new PricesController();
                        d = transaction.getTotalCashAmount().divide(pricesController.Price(transaction.getTicker(), transaction.getTimestamp()), 4);
                        c = c.add(d);
                        shares.put(transaction.getTicker(), c);
                    }
                }
            }
        }
        return shares;
    }

    public BigDecimal getValueOfShares(String InvestorId, List<Transaction> transactionList) {

    }
}
