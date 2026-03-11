package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.controller.PricesController;
import uk.co.asepstrath.bank.model.Transaction;

import java.math.BigDecimal;
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
        BigDecimal numberOfShares;
        BigDecimal currentNumberOfShares;
        for (Transaction transaction : transactionList) {
            if (transaction.getType().equals("BUY")) {
                if (transaction.getInvestorId().equals(InvestorId)) {
                    currentNumberOfShares = shares.getOrDefault(transaction.getTicker(), BigDecimal.ZERO);
                        PricesController pricesController = new PricesController();
                        numberOfShares = transaction.getTotalCashAmount().divide(pricesController.Price(transaction.getTicker(), transaction.getTimestamp()), 4);
                        currentNumberOfShares = currentNumberOfShares.add(numberOfShares);
                        shares.put(transaction.getTicker(), currentNumberOfShares);
                }
            } else if (transaction.getType().equals("SELL")) {
                currentNumberOfShares = shares.getOrDefault(transaction.getTicker(), BigDecimal.ZERO);
                PricesController pricesController = new PricesController();
                numberOfShares = transaction.getTotalCashAmount().divide(pricesController.Price(transaction.getTicker(), transaction.getTimestamp()), 4);
                currentNumberOfShares = currentNumberOfShares.subtract(numberOfShares);
                shares.put(transaction.getTicker(), currentNumberOfShares);
            }
        }
        return shares;
    }

    public BigDecimal getValueOfShares(String InvestorId, List<Transaction> transactionList) {
        HashMap<String, BigDecimal> shares = new HashMap<>();
        BigDecimal value = BigDecimal.ZERO;
        shares = getNumberOfShares(InvestorId, transactionList);
        for (String ticker : shares.keySet()) {
            PricesController pricesController = new PricesController();
            PriceService priceService = new PriceService();
            value = value.add( shares.get(ticker).multiply(priceService.getMostRecent(pricesController.Prices(ticker)).getPrice()));
        }
        return value;
    }

    public BigDecimal getValueOfSharesByTicker(String InvestorId, List<Transaction> transactionList, String shareTicker) {
        HashMap<String, BigDecimal> shares = new HashMap<>();
        BigDecimal value = BigDecimal.ZERO;
        shares = getNumberOfShares(InvestorId, transactionList);
        for (String ticker : shares.keySet()) {
            PricesController pricesController = new PricesController();
            PriceService priceService = new PriceService();
            if (ticker.equals(shareTicker)) {
                value = value.add(shares.get(ticker).multiply(priceService.getMostRecent(pricesController.Prices(ticker)).getPrice()));
            }
        }
        return value;
    }
}
