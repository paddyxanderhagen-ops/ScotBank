package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.util.List;

public class TransactionService {

    private List<Transaction> transactionList = null;
    private TransactionDTO transactionDTO;

    public TransactionService () {}

    public  TransactionService (TransactionDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
    }

    public String getById (String id, List<Transaction> transactionList) {
        for (Transaction transaction : transactionList) {
            if (transaction.getId().equals(id)) {
                return "id: " + transaction.getId() + "\ntimestamp: " + transaction.getTimestamp() + "\ntype: "  + transaction.getType() + "\ninvestorId: " + transaction.getInvestorId() + "\nticker: " + transaction.getTicker() + "\ntotalCashAmount: " + transaction.getTotalCashAmount() + "\n\n";
            }
        }
        return "Transaction:" + id + " not found";
    }

    public void getAll(){
        List<Transaction> transactionList = transactionDTO.getTransactions();
    }
}
