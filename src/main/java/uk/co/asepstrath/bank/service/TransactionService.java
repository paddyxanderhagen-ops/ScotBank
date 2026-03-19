package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import uk.co.asepstrath.bank.model.Price;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private TransactionRepository repo;
    private List<Transaction> transactions;
    private PriceService priceService;
    private APIService api;

    public TransactionService() {
        transactions = new ArrayList<>();
    }

    public TransactionService(TransactionDTO transactionDTO) {
        transactions = transactionDTO.getTransactions();
    }

    public TransactionService(TransactionRepository repo, PriceService priceService, APIService api) {
        this.repo = repo;
        this.priceService = priceService;
        this.api = api;
    }

    public String getById(String id, List<Transaction> transactionList) {
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                return "id: " + transaction.getId() + "\ntimestamp: " + transaction.getTimestamp() + "\ntype: " + transaction.getType() + "\ninvestorId: " + transaction.getInvestorId() + "\nticker: " + transaction.getTicker() + "\ntotalCashAmount: " + transaction.getTotalCashAmount() + "\n\n";
            }
        }
        return "Transaction:" + id + " not found";
    }

    public HashMap<String, BigDecimal> getNumberOfShares(String investorId, List<Transaction> transactionList) {
        HashMap<String, BigDecimal> shares = new HashMap<>();

        for (Transaction transaction : transactionList) {
            if (transaction.getTicker() == null) continue;
            if (!transaction.getInvestorId().equals(investorId)) continue;

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
    }

    public BigDecimal getValueOfShares(String investorId, List<Transaction> transactionList) {
        HashMap<String, BigDecimal> shares = getNumberOfShares(investorId, transactionList);
        BigDecimal value = BigDecimal.ZERO;

        for (String ticker : shares.keySet()) {
            List<Price> prices = api.getPricesForStock(ticker); // из кэша
            BigDecimal latestPrice = priceService.getLatestPrice(prices);
            value = value.add(shares.get(ticker).multiply(latestPrice));
        }
        return value;
    }

    public BigDecimal getValueOfSharesByTicker(String investorId, List<Transaction> transactionList, String shareTicker) {
        HashMap<String, BigDecimal> shares = getNumberOfShares(investorId, transactionList);
        BigDecimal value = BigDecimal.ZERO;

        for (String ticker : shares.keySet()) {
            if (ticker.equals(shareTicker)) {
                List<Price> prices = api.getPricesForStock(ticker); // из кэша
                BigDecimal latestPrice = priceService.getLatestPrice(prices);
                value = value.add(shares.get(ticker).multiply(latestPrice));
            }
        }
        return value;
    }

    public List<Transaction> getAll() {
        return transactions;
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
}
