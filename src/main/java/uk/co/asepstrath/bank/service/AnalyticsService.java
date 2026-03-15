package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.controller.PricesController;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.*;

public class AnalyticsService {
    private TransactionRepository repo;
    private PriceService priceService;
    private PricesController pricesController;


    public AnalyticsService (TransactionRepository repo, PriceService priceService) {
        this.repo = repo;
        this.priceService = priceService;
        pricesController = new PricesController();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


    public List<Position> getPositions(String investorId) {

        List<Transaction> transactions =
                repo.getTransactionsByInvestor(investorId);

        Map<String, List<Transaction>> grouped = new HashMap<>();

        for (Transaction t : transactions) {

            if (t==null || t.getTicker() == null || t.getTicker().isBlank())
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
        double buyCash = 0;

        List<Price> prices = pricesController.Prices(ticker);

        for (Transaction t : transactions) {

            if (t == null || t.getTicker() == null)
                continue;

            BigDecimal p = priceService.getPrice(t.getTimestamp(), prices);
            if (p == null)
                continue;

            double price = p.doubleValue();
            double numberOfShares = t.getTotalCashAmount() / price;

            if (t.getType().equals("BUY")) {

                shares += numberOfShares;
                buyShares += numberOfShares;
                buyCash += t.getTotalCashAmount();

            } else if (t.getType().equals("SELL")) {

                shares -= numberOfShares;

            }
        }

        if (shares <= 0)
            return null;

        double avgBuyPrice = buyCash / buyShares;

        double lastPrice =
                priceService
                        .getLatestPrice(prices)
                        .doubleValue();

        double currentCash = lastPrice * shares;

        return new Position(
                ticker,
                round(shares),
                round(avgBuyPrice),
                round(lastPrice),
                round(buyCash),
                round(currentCash)
        );
    }

}
