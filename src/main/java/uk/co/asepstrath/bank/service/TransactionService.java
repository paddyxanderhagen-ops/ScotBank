package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private List<Transaction> transactionList = new ArrayList<>();
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
        transaction.setId(transactionId);
        transaction.setTimestamp(timestamp);
        transaction.setType(type);
        transaction.setInvestorId(investorId);
        transaction.setTicker(ticker);
        transaction.setTotalCashAmount(BigDecimal.valueOf(amount));

        transactionList.add(transaction);
    }
}
