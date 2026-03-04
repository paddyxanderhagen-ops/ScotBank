package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.util.List;

public class TransactionService {

    private List<Transaction> transactionList;

    public String getById (String id, List<Transaction> transactionList ) {
        for (Transaction transaction : transactionList) {
            if (transaction.getId().equals(id)) {
                return "id: " + transaction.getId() + "\ntimestamp: " + transaction.getTimestamp() + "\ntype: "  + transaction.getType() + "\ninvestorId: " + transaction.getInvestorId() + "\nticker: " + transaction.getTicker() + "\ntotalCashAmount: " + transaction.getTotalCashAmount() + "\n\n";
            }
        }
        return "Transaction:" + id + " not found";
    }

    public List<Transaction> getAll(){
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/transactions").accept("application/json").asString().getBody();

        TransactionDTO transactionResponse = gson.fromJson(response, TransactionDTO.class);

        List<Transaction> transactionList = transactionResponse.getTransactions();

        return transactionList;
    }
}
