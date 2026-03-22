package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.*;
import uk.co.asepstrath.bank.repository.PriceRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class APIService {

    private StringBuilder url;
    private final Gson gson;

    public APIService() {
        url = new StringBuilder("https://api.asep-strath.co.uk/api/");
        gson = new Gson();
    }

    public InvestorDTO getAllInvestor() {
        url.append("investors");
        String response = Unirest.get(url.toString()).accept("application/json").asString().getBody();

        InvestorDTO investorDTO = gson.fromJson(response, InvestorDTO.class);

        url = new StringBuilder("https://api.asep-strath.co.uk/api/");

        if (investorDTO.getInvestors().isEmpty()) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Could not retrieve investors");
        }

        return investorDTO;
    }

    public TransactionDTO getAllTransaction() {
        String nextToken = "";
        List<Transaction> transactions = new ArrayList<>();
        TransactionDTO transactionDTO = new TransactionDTO();

        while (nextToken != null) {
            url.append("transactions?nextToken=").append(nextToken);
            String response = Unirest.get(url.toString()).accept("application/json").asString().getBody();
            transactionDTO = gson.fromJson(response, TransactionDTO.class);
            transactions.addAll(transactionDTO.getTransactions());
            nextToken = transactionDTO.getPages().getNextToken();
            url = new StringBuilder("https://api.asep-strath.co.uk/api/");
        }

        transactionDTO.setTransactions(transactions);

        if (transactionDTO.getTransactions().isEmpty()) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Could not retrieve transactions");
        }

        return transactionDTO;
    }

    public PriceDTO getAllPrices (String ticker, PriceRepository repo, String nextToken) {
        List<Price> prices = new ArrayList<>();
        PriceDTO priceDTO = new PriceDTO();
        String prevTok = "";

        while (nextToken != null) {
            prevTok = nextToken;
            url.append("prices/").append(ticker).append("?nextToken=").append(nextToken);
            String response = Unirest.get(url.toString()).accept("application/json").asString().getBody();
            priceDTO = gson.fromJson(response, PriceDTO.class);
            prices.addAll(priceDTO.getPrices());

            try {
                repo.saveAll(ticker, priceDTO.getPrices());
            } catch (SQLException e) {
                throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to save prices");
            }

            nextToken = priceDTO.getPages().getNextToken();

            url = new StringBuilder("https://api.asep-strath.co.uk/api/");
        }

        priceDTO.setPrices(prices);

        saveToken(prevTok);

        return priceDTO;
    }

    private void saveToken(String token) {
        try {
            Files.writeString(Path.of("./data/token.txt"), token);
        } catch (IOException e) {
            System.err.println("Failed to save token: " + e.getMessage());
        }
    }

    public String loadToken() {
        Path path = Path.of("./data/token.txt");
        try {
            if (Files.exists(path)) {
                return Files.readString(path);
            }
        } catch (IOException e) {
            System.err.println("Failed to load token: " + e.getMessage());
        }
        return "";
    }


    public EquitiesDTO getAllEquities() {
        url.append("equities");
        String response = Unirest.get(url.toString()).accept("application/json").asString().getBody();

        EquitiesDTO equityResponse = gson.fromJson(response, EquitiesDTO.class);

        url = new StringBuilder("https://api.asep-strath.co.uk/api/");

        return equityResponse;
    }

    public EtfDTO getAllEtf() {
        url.append("etfs");
        String response = Unirest.get(url.toString()).accept("application/json").asString().getBody();

        EtfDTO etfDTO = gson.fromJson(response, EtfDTO.class);

        url = new StringBuilder("https://api.asep-strath.co.uk/api/");

        return etfDTO;
    }
}
