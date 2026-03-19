package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class AnalyticsService {
    private TransactionRepository repo;
    private PriceService priceService;
    private APIService apiService;


    public AnalyticsService (TransactionRepository repo, PriceService priceService, APIService api) {
        this.repo = repo;
        this.priceService = priceService;
        this.apiService = api;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


    public List<Position> getPositions(String investorId) {
        try {
            List<Transaction> transactions =
                    repo.getTransactionsByInvestor(investorId);

            Map<String, List<Transaction>> grouped = new HashMap<>();

            for (Transaction t : transactions) {

                if (t == null || t.getTicker() == null || t.getTicker().isBlank())
                    continue;

                grouped
                        .computeIfAbsent(t.getTicker(), k -> new ArrayList<>())
                        .add(t);
            }

            List<Position> positions = new ArrayList<>();

            for (String ticker : grouped.keySet()) {

                Position position =
                        calculatePosition(ticker, grouped.get(ticker));

                if (position != null) {
                    positions.add(position);
                }

            }

            return positions;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve transactions");
        }
    }

    public Position getPosition(String ticker, String investorId) {
        List <Position> positions = getPositions(investorId);
        for (Position p : positions) {
            if (p.getTicker().equals(ticker)) {
                return p;
            }
        }
        return null;
    }

    private Position calculatePosition(String ticker, List<Transaction> transactions) {
        double shares = 0;
        double buyShares = 0;
        double totalBuyCash = 0;
        double realisedPnl = 0;
        double avgBuyPrice = 0;

        List<Price> prices = apiService.getPricesForStock(ticker);

        for (Transaction t : transactions) {
            if (t == null || t.getTicker() == null) continue;

            BigDecimal p = priceService.getPrice(t.getTimestamp(), prices);
            if (p == null) continue;

            double price = p.doubleValue();
            double numberOfShares = t.getTotalCashAmount() / price;

            if (t.getType().equals("BUY")) {
                shares += numberOfShares;
                buyShares += numberOfShares;
                totalBuyCash += t.getTotalCashAmount();
                avgBuyPrice = totalBuyCash / buyShares;

            } else if (t.getType().equals("SELL")) {
                realisedPnl += (price - avgBuyPrice) * numberOfShares;
                shares -= numberOfShares;
            }
        }

        if (shares <= 0 && realisedPnl == 0) return null;

        double lastPrice = priceService.getLatestPrice(prices).doubleValue();
        double currentCash = lastPrice * shares;

        return new Position(
                ticker,
                round(shares),
                round(avgBuyPrice),
                round(lastPrice),
                round(avgBuyPrice * shares),
                round(currentCash),
                round(realisedPnl),
                round(totalBuyCash));
    }

}
