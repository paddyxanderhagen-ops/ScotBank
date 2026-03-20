package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.repository.PriceRepository;

import java.sql.SQLException;
import java.util.List;

public class PriceService {

    private PriceRepository repo;

    public PriceService(PriceRepository repo) {
        this.repo = repo;
    }

    public double getLatestPrice() {
        try {
            double price = repo.getLatestPrice();

            if (price == Double.MAX_VALUE) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Price not found");
            }

            return price;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve price");
        }
    }

    public double getPriceAtTimestamp(String timestamp) {
        try {
            double price = repo.getPriceAtTime(timestamp);

            if (price == Double.MAX_VALUE) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Price not found");
            }

            return price;

        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve price");
        }
    }

    public List<Double> getPrices(String ticker) {
        try {
            List<Double> prices = repo.getPricesForStock(ticker);

            if (prices == null || prices.isEmpty()) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Prices not found");
            }
            return prices;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve prices");
        }
    }
}