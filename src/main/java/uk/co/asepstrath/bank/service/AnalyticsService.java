package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.TransactionUtility;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.model.TaxReport;
import uk.co.asepstrath.bank.model.TaxSaleLine;
import uk.co.asepstrath.bank.model.Transaction;

import java.util.*;

public class AnalyticsService {

    private final TransactionService transactionService;
    private final PriceService priceService;
    private final InvestorService investorService;

    public AnalyticsService(TransactionService transactionService, PriceService priceService, InvestorService investorService) {
        this.transactionService = transactionService;
        this.priceService = priceService;
        this.investorService = investorService;
    }

    public List<Position> getPositions(String id) {
        List<Transaction> all = transactionService.getTransactionsByInvestor(id);
        double currentBalance = investorService.getInvestor(id).getCashBalance();
        List<Transaction> executed = TransactionUtility.getExecuted(all, currentBalance);

        Map<String, List<Transaction>> transactionsByTicker = new HashMap<>();
        for (Transaction t : executed) {
            if (t == null || t.getTicker() == null || t.getTicker().isBlank()) continue;
            transactionsByTicker.computeIfAbsent(t.getTicker(), k -> new ArrayList<>()).add(t);
        }

        List<Position> positions = new ArrayList<>();
        for (String ticker : transactionsByTicker.keySet()) {
            Position position = calculatePosition(ticker, transactionsByTicker.get(ticker));
            if (position != null) positions.add(position);
        }

        return positions;
    }

    public Position getPosition(String ticker, String investorId) {
        List<Position> positions = getPositions(investorId);
        for (Position p : positions) {
            if (p.getTicker().equals(ticker)) return p;
        }
        return null;
    }

    private Position calculatePosition(String ticker, List<Transaction> transactions) {
        double shares = 0;
        double totalBuyValue = 0;
        double buyShares = 0;
        double realisedPnl = 0;
        double avgBuyPrice = 0;

        for (Transaction t : transactions) {
            if (t == null || t.getTicker() == null) continue;

            double price = priceService.getPriceAtTimestamp(t.getTimestamp(), t.getTicker());
            double numberOfShares = t.getTotalCashAmount() / price;

            if (t.getType().equals("BUY")) {
                shares += numberOfShares;
                buyShares += numberOfShares;
                totalBuyValue += t.getTotalCashAmount();
                avgBuyPrice = totalBuyValue / buyShares;
            } else if (t.getType().equals("SELL")) {
                realisedPnl += (price - avgBuyPrice) * numberOfShares;
                shares -= numberOfShares;
            }
        }

        if (Math.abs(shares) < 0.0001) {
            return null;
        }
        double currPrice = priceService.getLatestPrice(ticker);


        double costBasis = avgBuyPrice * shares;
        double currentValue = currPrice * shares;

        shares = Math.round(shares * 100.0) / 100.0;
        costBasis = Math.round(costBasis * 100.0) / 100.0;
        currentValue = Math.round(currentValue * 100.0) / 100.0;
        realisedPnl = Math.round(realisedPnl * 100.0) / 100.0;



        return new Position(ticker, shares, currPrice, costBasis, currentValue, realisedPnl, avgBuyPrice, totalBuyValue);
    }

    public TaxReport getTaxReport(String investorId, int year) {
        List<Transaction> all = transactionService.getTransactionsByInvestor(investorId);
        double currentBalance = investorService.getInvestor(investorId).getCashBalance();
        List<Transaction> executed = TransactionUtility.getExecuted(all, currentBalance);

        Map<String, Double> avgBuyPrices = new HashMap<>();
        Map<String, Double> totalShares = new HashMap<>();
        Map<String, Double> totalCash = new HashMap<>();
        List<TaxSaleLine> sales = new ArrayList<>();

        for (Transaction t : executed) {
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
        List<Transaction> all = transactionService.getTransactionsByInvestor(investorId);
        double currentBalance = investorService.getInvestor(investorId).getCashBalance();
        List<Transaction> executed = TransactionUtility.getExecuted(all, currentBalance);

        return executed.stream()
                .filter(t -> "SELL".equals(t.getType()))
                .map(t -> Integer.parseInt(t.getTimestamp().substring(0, 4)))
                .distinct()
                .sorted()
                .toList();
    }
}