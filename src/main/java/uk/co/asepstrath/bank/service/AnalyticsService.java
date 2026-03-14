package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.controller.PricesController;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;

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

    public List<Position> getPositions(String investorId) {

        List<Transaction> transactions =
                repo.getTransactionsByInvestor(investorId);

        Map<String, List<Transaction>> grouped = new HashMap<>();

        for (Transaction t : transactions) {

            if (t.getTicker() == null)
                continue;

            grouped
                    .computeIfAbsent(t.getTicker(), k -> new ArrayList<>())
                    .add(t);
        }

        List<Position> positions = new ArrayList<>();

        for (String ticker : grouped.keySet()) {

            Position position =
                    calculatePosition(ticker, grouped.get(ticker));

            positions.add(position);
        }

        return positions;
    }
    private Position calculatePosition(String ticker, List<Transaction> txs) {

        double shares = 0;
        double buyCash = 0;

        for (Transaction t : txs) {

            List<Price> prices = pricesController.Prices(t.getTicker());

            double price =
                    priceService
                            .getPrice(t.getTimestamp(), prices)
                            .doubleValue();


            double numberOfShares =
                    t.getTotalCashAmount() / price;

            if (t.getType().equals("BUY")) {

                shares += numberOfShares;
                buyCash += t.getTotalCashAmount();

            } else if (t.getType().equals("SELL")) {

                shares -= numberOfShares;
            }
        }
        double avgBuyPrice;
        if(shares!=0) {
            avgBuyPrice = buyCash / shares;
        }
        else return null;

        List<Price> prices = pricesController.Prices(ticker);

        double lastPrice =
                priceService
                        .getLatestPrice(prices)
                        .doubleValue();


        double currentCash = lastPrice * shares;

        return new Position(
                ticker,
                (int) shares,
                avgBuyPrice,
                lastPrice,
                buyCash,
                currentCash
        );
    }

}
