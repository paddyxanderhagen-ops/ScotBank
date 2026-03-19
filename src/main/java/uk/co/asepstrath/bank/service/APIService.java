package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.PriceDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class APIService {

    private StringBuilder url;
    private Gson gson;
    private final Map<String, List<Price>> cache = new ConcurrentHashMap<>();

    public APIService () {
        url = new StringBuilder("https://api.asep-strath.co.uk/api/");
        gson = new Gson();
    }

    public InvestorDTO getAllInvestor () {
        url.append("investors");
        String response =  Unirest.get(url.toString()).accept("application/json").asString().getBody();

        InvestorDTO investorDTO = gson.fromJson(response, InvestorDTO.class);
        int i = url.indexOf("investors");
        url.delete(i, url.length());
        return investorDTO;
    }

    public TransactionDTO getAllTransaction () {
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

        for (Transaction transaction : transactions) {
            transactionDTO.addTransaction(transaction);
        }

        return transactionDTO;
    }

    public List<Price> getPricesForStock(String ticker) {
        if (cache.containsKey(ticker)) {
            return cache.get(ticker);
        }

        String nextToken = null;
        List<Price> prices = new ArrayList<>();

        do {
            StringBuilder requestUrl = new StringBuilder("https://api.asep-strath.co.uk/api/prices/")
                    .append(ticker);

            if (nextToken != null) {
                requestUrl.append("?nextToken=").append(nextToken);
            }

            String response = Unirest.get(requestUrl.toString()).accept("application/json").asString().getBody();
            PriceDTO priceDTO = gson.fromJson(response, PriceDTO.class);

            if (priceDTO == null || priceDTO.getPriceList() == null) break;

            prices.addAll(priceDTO.getPriceList());
            nextToken = (priceDTO.getPages() != null) ? priceDTO.getPages().getNextToken() : null;

        } while (nextToken != null);

        cache.put(ticker, prices);
        return prices;
    }
}
