package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.model.TaxReport;
import uk.co.asepstrath.bank.model.TaxSaleLine;
import uk.co.asepstrath.bank.model.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {

    private TransactionService transactionService;
    private PriceService priceService;

    public AnalyticsService (TransactionService transactionService, PriceService priceService) {
        this.transactionService = transactionService;
        this.priceService = priceService;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


    public List<Position> getPositions(String id) {
        List<Transaction> transactions = transactionService.getTransactionsByInvestor(id);
        Map<String, List<Transaction>> transactionsByTicker = new HashMap<>();

        for (Transaction t : transactions) {
            if (t == null || t.getTicker() == null || t.getTicker().isBlank()) {
                continue;
            }

            transactionsByTicker.computeIfAbsent(t.getTicker(), k -> new ArrayList<>()).add(t);
        }

        List<Position> positions = new ArrayList<>();

        for (String ticker : transactionsByTicker.keySet()) {
            Position position = calculatePosition(ticker, transactionsByTicker.get(ticker));

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
        double totalBuyCash = 0;
        double realisedPnl = 0;
        double avgBuyPrice = 0;



        for (Transaction t : transactions) {
            if (t == null || t.getTicker() == null) continue;

            System.out.println(t.getTicker());


            double price = priceService.getPriceAtTimestamp(t.getTimestamp(), t.getTicker());
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

        double lastPrice = priceService.getLatestPrice(ticker);
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
    public TaxReport getTaxReport(String investorId, int year) {
        List<Transaction> transactions = transactionService.getTransactionsByInvestor(investorId);

        Map<String, Double> avgBuyPrices = new HashMap<>();
        Map<String, Double> totalShares = new HashMap<>();
        Map<String, Double> totalCash = new HashMap<>();

        List<TaxSaleLine> sales = new ArrayList<>();

        transactions.sort(Comparator.comparing(Transaction::getTimestamp));

        for (Transaction t : transactions) {
            if (t.getTicker() == null || t.getTicker().isBlank()) continue;

            int txYear = Integer.parseInt(t.getTimestamp().substring(0, 4));
            String ticker = t.getTicker();
            double price = priceService.getPriceAtTimestamp(t.getTimestamp(), ticker);
            double numberOfShares = t.getTotalCashAmount() / price;

            if (t.getType().equals("BUY")) {
                double prevShares = totalShares.getOrDefault(ticker, 0.0);
                double prevCash = totalCash.getOrDefault(ticker, 0.0);
                totalShares.put(ticker, prevShares + numberOfShares);
                totalCash.put(ticker, prevCash + t.getTotalCashAmount());
                avgBuyPrices.put(ticker, totalCash.get(ticker) / totalShares.get(ticker));

            } else if (t.getType().equals("SELL") && txYear == year) {
                double avgBuy = avgBuyPrices.getOrDefault(ticker, price);
                double costBasis = avgBuy * numberOfShares;
                double proceeds = t.getTotalCashAmount();

                sales.add(new TaxSaleLine(ticker, t.getTimestamp(), proceeds, costBasis));

                double prevShares = totalShares.getOrDefault(ticker, 0.0);
                totalShares.put(ticker, prevShares - numberOfShares);
            }
        }

        return new TaxReport(year, sales);
    }

    public List<Integer> getAvailableYears(String investorId) {
        List<Transaction> transactions = transactionService.getTransactionsByInvestor(investorId);
        return transactions.stream()
                .filter(t -> "SELL".equals(t.getType()))
                .map(t -> Integer.parseInt(t.getTimestamp().substring(0, 4)))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
