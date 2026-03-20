package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.EquitiesDTO;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.PriceDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.repository.PriceRepository;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.ArrayList;
import java.util.List;

public class APIService {

    private StringBuilder url;
    private final Map<String, List<Price>> cache = new ConcurrentHashMap<>();
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

    public PriceDTO getAllPrices (String ticker, PriceRepository repo, Equity equity) throws SQLException {
        List<Price> prices = new ArrayList<>();
        PriceDTO priceDTO = new PriceDTO();
        String nextToken = loadToken();
        String prevTok = "";

        while (nextToken != null) {
            prevTok = nextToken;
            url.append("prices/").append(ticker).append("?nextToken=").append(nextToken);
            System.out.println(url);
            String response = Unirest.get(url.toString()).accept("application/json").asString().getBody();
            priceDTO = gson.fromJson(response, PriceDTO.class);
            repo.saveAll(equity, priceDTO.getPrices());
            nextToken = priceDTO.getPages().getNextToken();

            url = new StringBuilder("https://api.asep-strath.co.uk/api/");
        }

        priceDTO.setPrices(prices);

        if (ticker.equals("APM")) {
            saveToken(prevTok);
        }

        return priceDTO;
    }

    private void saveToken(String token) {
        try {
            Files.writeString(Path.of("./data/token.txt"), token);
        } catch (IOException e) {
            System.err.println("Failed to save token: " + e.getMessage());
        }
    }

    private String loadToken() {
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


    public EquitiesDTO getAlEquities () {
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/equities").accept("application/json").asString().getBody();

        EquitiesDTO equityResponse = gson.fromJson(response, EquitiesDTO.class);

        return equityResponse;
    }
}
