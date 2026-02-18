package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import kong.unirest.core.QueryParams;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.List;

@Path("/transactions")
public class TransactionController {

    @GET
    public String transaction(@QueryParam String id){
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/transactions").accept("application/json").asString().getBody();

        TransactionDTO transactionResponse = gson.fromJson(response, TransactionDTO.class);

        List<Transaction> transactionList = transactionResponse.getTransactions();

        TransactionService transactionService = new TransactionService();

        return transactionService.getById(id, transactionList);
    }


}
