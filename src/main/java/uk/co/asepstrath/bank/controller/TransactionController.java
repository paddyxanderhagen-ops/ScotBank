package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.*;
import java.util.stream.Collectors;

@Path("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GET("/ViewAll")
    public ModelAndView<Map<String, Object>> getTransactionsForInvestor(@QueryParam String id, Context ctx) {
        List<Transaction> transactions = transactionService.getTransactionsByInvestor(id);

        List<Transaction> sorted = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());

        double runningBalance = 0;
        double totalDeposits = 0;
        double totalWithdrawals = 0;
        List<Map<String, Object>> enriched = new ArrayList<>();

        for (Transaction t : sorted) {
            String balanceAfterStr = "—";

            if (t.getType().equals("DEPOSIT")) {
                runningBalance += t.getTotalCashAmount();
                totalDeposits += t.getTotalCashAmount();
                balanceAfterStr = String.valueOf(Math.round(runningBalance * 100.0) / 100.0);
            } else if (t.getType().equals("WITHDRAWAL")) {
                runningBalance -= t.getTotalCashAmount();
                totalWithdrawals += t.getTotalCashAmount();
                balanceAfterStr = String.valueOf(Math.round(runningBalance * 100.0) / 100.0);
            }

            Map<String, Object> row = new HashMap<>();
            row.put("timestamp", t.getTimestamp());
            row.put("type", t.getType());
            row.put("ticker", t.getTicker() != null ? t.getTicker() : "");
            row.put("amount", t.getTotalCashAmount());
            row.put("balanceAfter", balanceAfterStr);
            enriched.add(row);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("transactions", enriched);
        model.put("totalDeposits", Math.round(totalDeposits * 100.0) / 100.0);
        model.put("totalWithdrawals", Math.round(totalWithdrawals * 100.0) / 100.0);
        model.put("investorId", id);

        return new ModelAndView("transactions.hbs", model);
    }
}
