package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.util.ArrayList;
import java.util.List;

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

        for (Transaction transaction : transactions) {
            transactionDTO.addTransaction(transaction);
        }

        if (transactionDTO.getTransactions().isEmpty()) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Could not retrieve transactions");
        }

        return transactionDTO;
    }
}
