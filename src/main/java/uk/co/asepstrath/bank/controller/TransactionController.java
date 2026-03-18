package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GET
    public String transaction(@QueryParam String id){
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/transactions").accept("application/json").asString().getBody();

        TransactionDTO transactionResponse = gson.fromJson(response, TransactionDTO.class);

        List<Transaction> transactionList = transactionResponse.getTransactions();

        TransactionService transactionService = new TransactionService();

        return transactionService.getById(id, transactionList);
    }
    @GET("/test")
    public List<Transaction> getTransactionsForInvestor (@QueryParam String id) {

        List<Transaction> transactions = transactionService.getTransactionsByInvestor(id);
        return transactions;
    }

    @GET("/Shares")
    public HashMap<String, BigDecimal> investor(@QueryParam String investorId){
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/transactions").accept("application/json").asString().getBody();

        TransactionDTO transactionResponse = gson.fromJson(response, TransactionDTO.class);

        List<Transaction> transactionList = transactionResponse.getTransactions();

        TransactionService transactionService = new TransactionService();

        return (transactionService.getNumberOfShares(investorId, transactionList));
    }

    @GET("/Shares/Value")
    public BigDecimal value(@QueryParam String investorId){
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/transactions").accept("application/json").asString().getBody();

        TransactionDTO transactionResponse = gson.fromJson(response, TransactionDTO.class);

        List<Transaction> transactionList = transactionResponse.getTransactions();

        TransactionService transactionService = new TransactionService();

        return transactionService.getValueOfShares(investorId, transactionList);
    }
}
