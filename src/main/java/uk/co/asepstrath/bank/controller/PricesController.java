package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.annotation.Path;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/prices")
public class PricesController {

    private StringBuilder url = new StringBuilder("https://api.asep-strath.co.uk/api/");
    private TransactionDTO transactionDTO;
    private List<Transaction> transactions = new ArrayList<>();
    private Gson gson = new Gson();
    private final Map<String, List<Price>> cache = new HashMap<>();

}
