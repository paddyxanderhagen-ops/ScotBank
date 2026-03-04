package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {

    private List<Transaction> transactionList = null;
    private TransactionDTO transactionDTO;

    public TransactionService () {}

    public  TransactionService (TransactionDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
    }

    public String getById (String id) {
        for (Transaction transaction : transactionList) {
            if (transaction.getId().equals(id)) {
                return "id: " + transaction.getId() + "\ntimestamp: " + transaction.getTimestamp() + "\ntype: "  + transaction.getType() + "\ninvestorId: " + transaction.getInvestorId() + "\nticker: " + transaction.getTicker() + "\ntotalCashAmount: " + transaction.getTotalCashAmount() + "\n\n";
            }
        }
        return "Transaction:" + id + " not found";
    }

    public List<Transaction> getAll(){
        transactionList = transactionDTO.getTransactions();
        return transactionList;
    }

    public void createTransaction (String transactionId, String timestamp, String type, String investorId, String ticker, double amount) {
        Transaction transaction = new Transaction();
        transaction.setId("123");
        transaction.setTimestamp("12:00");
        transaction.setType("Deposit");
        transaction.setInvestorId("JB123");
        transaction.setTicker("BNNA");
        transaction.setTotalCashAmount(BigDecimal.valueOf(1000.0));

        transactionList.add(transaction);
    }
}
