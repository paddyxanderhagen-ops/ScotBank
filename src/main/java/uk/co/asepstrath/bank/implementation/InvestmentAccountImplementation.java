package uk.co.asepstrath.bank.implementation;

import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Stock;
import uk.co.scotbank.InvestmentAccount;

public class InvestmentAccountImplementation implements InvestmentAccount {
    private Investor investor;

    public InvestmentAccountImplementation(Investor investor) {
        this.investor = investor;
    }
    public InvestmentAccountImplementation() {
        this.investor = new Investor();
    }

    @Override
    public void buyStock(String ticker, int amount, double price) {
        validateStockInput(ticker, amount, price);

        checkDecimals(price);

        Stock stock = new Stock(ticker,amount,price*amount, true);
        investor.setStocks(stock);
    }

    @Override
    public void sellStock(String ticker, int amount, double price) {
        validateStockInput(ticker, amount, price);

        if (getShares(ticker) < amount) {
            throw new IllegalArgumentException("Insufficient shares");
        }

        checkDecimals(price);

        Stock stock = new Stock(ticker, -amount, 0, false);
        investor.setStocks(stock);
    }

    @Override
    public int getShares(String ticker) {
        int shares = 0;

        for (Stock stock : investor.getStocks()) {
            if (stock.getTicker().equals(ticker)) {
                shares += stock.getAmount();
            }
        }

        return shares;
    }

    @Override
    public double getBookPrice(String ticker) {
        double bookPrice = 0;
        int shares = 0;

        for (Stock stock : investor.getStocks()) {
            if (stock.getTicker().equals(ticker) && stock.isBuy()) {
                bookPrice+=stock.getPrice();
                shares += stock.getAmount();
            }
        }

        if (shares > 0) {
            return bookPrice / shares;
        } else return 0;
    }

    private void checkDecimals (double value) {
        String stringV = Double.toString(Math.abs(value));
        int integerPlaces = stringV.indexOf('.');
        int decimalPlaces = stringV.length() - integerPlaces - 1;

        if (decimalPlaces > 2) {
            throw new IllegalArgumentException("Cannot accept more than 2 decimal places");
        }
    }

    private void validateStockInput(String ticker, int amount, double price) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        if (ticker == null || ticker.isEmpty()) {
            throw new IllegalArgumentException("Ticker cannot be null");
        }
    }
}
