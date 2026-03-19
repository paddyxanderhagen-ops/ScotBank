package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import uk.co.asepstrath.bank.model.Price;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private TransactionRepository repo;


    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }


    public List<Transaction> getTransactionsByInvestor(String id) {
        try {
            List<Transaction> transactions = repo.getTransactionsByInvestor(id);

            if (transactions == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Transactions not found for investor");
            }
            return transactions;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve transactions");
        }
    }


    public HashMap<String, BigDecimal> getNumberOfShares(String id) {
        HashMap<String, BigDecimal> shares = new HashMap<>();

        try{
            List<Transaction> transactions = repo.getTransactionsByInvestor(id);
            PriceService priceService = new PriceService();
            APIService api = new APIService();

            if (transactions == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "No shares found for investor");
            }

            for (Transaction transaction : transactions) {
                if (transaction.getTicker() == null) continue;
                if (!transaction.getInvestorId().equals(id)) continue;

                List<Price> prices = api.getPricesForStock(transaction.getTicker()); // из кэша
                BigDecimal price = priceService.getPrice(transaction.getTimestamp(), prices);
                BigDecimal numberOfShares = BigDecimal.valueOf(transaction.getTotalCashAmount())
                        .divide(price, 4, RoundingMode.HALF_UP);

                BigDecimal current = shares.getOrDefault(transaction.getTicker(), BigDecimal.ZERO);

                if (transaction.getType().equals("BUY")) {
                    shares.put(transaction.getTicker(), current.add(numberOfShares));
                } else if (transaction.getType().equals("SELL")) {
                    shares.put(transaction.getTicker(), current.subtract(numberOfShares));
                }
            }
            return shares;

        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve shares");
        }
    }

//    public BigDecimal getValueOfShares(String investorId, List<Transaction> transactionList) {
//        HashMap<String, BigDecimal> shares = getNumberOfShares(investorId, transactionList);
//        BigDecimal value = BigDecimal.ZERO;
//
//        for (String ticker : shares.keySet()) {
//            List<Price> prices = api.getPricesForStock(ticker); // из кэша
//            BigDecimal latestPrice = priceService.getLatestPrice(prices);
//            value = value.add(shares.get(ticker).multiply(latestPrice));
//        }
//        return value;
//    }
//
//    public BigDecimal getValueOfSharesByTicker(String investorId, List<Transaction> transactionList, String shareTicker) {
//        HashMap<String, BigDecimal> shares = getNumberOfShares(investorId, transactionList);
//        BigDecimal value = BigDecimal.ZERO;
//
//        for (String ticker : shares.keySet()) {
//            if (ticker.equals(shareTicker)) {
//                List<Price> prices = api.getPricesForStock(ticker); // из кэша
//                BigDecimal latestPrice = priceService.getLatestPrice(prices);
//                value = value.add(shares.get(ticker).multiply(latestPrice));
//            }
//        }
//        return value;
//    }
}
