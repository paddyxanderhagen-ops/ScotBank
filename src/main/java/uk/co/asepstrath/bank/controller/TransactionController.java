package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.Context;
import io.jooby.ModelAndView;
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
import java.util.Map;

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
    @GET("/ViewAll")
    public ModelAndView getTransactionsForInvestor(@QueryParam String id, Context ctx) {
        List<Transaction> transactions = transactionService.getTransactionsByInvestor(id);
        Map<String, Object> model = new HashMap<>();
        model.put("transactions", transactions);
        model.put("investorId", id);
        return new ModelAndView("transactions.hbs", model);
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
