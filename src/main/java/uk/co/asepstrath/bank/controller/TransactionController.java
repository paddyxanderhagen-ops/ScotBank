package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import uk.co.asepstrath.bank.TransactionUtility;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.*;

@Path("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final InvestorService investorService;

    public TransactionController(TransactionService transactionService, InvestorService investorService) {
        this.transactionService = transactionService;
        this.investorService = investorService;
    }

    @GET("/view-all")
    public ModelAndView<Map<String, Object>> getTransactionsForInvestor(Context ctx) {
        List<Transaction> transactions = getSession(ctx);
        String id = ctx.session().get("id").value();

        double currentBalance = investorService.getInvestor(id).getCashBalance();

        List<Transaction> executed = TransactionUtility.getExecuted(transactions, currentBalance);

        double runningBalance = 0;

        for (Transaction t : executed) {
            if (t.getType().equals("DEPOSIT") || t.getType().equals("SELL")) {
                runningBalance -= t.getTotalCashAmount();
            } else if (t.getType().equals("WITHDRAWAL") || t.getType().equals("BUY")) {
                runningBalance += t.getTotalCashAmount();
            }
        }
        runningBalance += currentBalance;

        double totalDeposits = 0;
        double totalWithdrawals = 0;
        List<Map<String, Object>> enriched = new ArrayList<>();

        for (Transaction t : executed) {
            String balanceAfterStr = "—";

            if (t.getType().equals("DEPOSIT") || t.getType().equals("SELL")) {
                runningBalance += t.getTotalCashAmount();
                if (t.getType().equals("DEPOSIT")) {
                    totalDeposits += t.getTotalCashAmount();
                }
                balanceAfterStr = String.valueOf((Math.round((runningBalance)* 100.0) / 100.0));
            } else if (t.getType().equals("WITHDRAWAL") || t.getType().equals("BUY")) {
                runningBalance -= t.getTotalCashAmount();
                if (t.getType().equals("WITHDRAWAL")) {
                    totalWithdrawals += t.getTotalCashAmount();
                }
                balanceAfterStr = String.valueOf(Math.round((runningBalance) * 100.0) / 100.0);
            }

            Map<String, Object> row = new HashMap<>();
            row.put("timestamp", t.getTimestamp());
            row.put("type", t.getType());
            row.put("ticker", t.getTicker() != null ? t.getTicker() : "");
            row.put("amount", t.getTotalCashAmount());
            row.put("balanceAfter", balanceAfterStr);
            enriched.add(row);
        }

        List<Transaction> allSorted = TransactionUtility.getExecuted(transactions, currentBalance);
        for (Transaction t : allSorted) {
            if (!executed.contains(t) && (t.getType().equals("BUY") || t.getType().equals("WITHDRAWAL"))) {
                Map<String, Object> row = new HashMap<>();
                row.put("timestamp", t.getTimestamp());
                row.put("type", t.getType());
                row.put("ticker", t.getTicker() != null ? t.getTicker() : "");
                row.put("amount", t.getTotalCashAmount());
                row.put("balanceAfter", "insufficient funds");
                enriched.add(row);
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("transactions", enriched);
        model.put("totalDeposits", Math.round(totalDeposits * 100.0) / 100.0);
        model.put("totalWithdrawals", Math.round(totalWithdrawals * 100.0) / 100.0);
        model.put("id", id);
        return new ModelAndView<>("transactions.hbs", model);
    }

    private List<Transaction> getSession(Context ctx) {
        if (ctx == null) {
            return null;
        }

        Session session = ctx.session();

        List<Transaction> transactions = null;

        if (!session.get("id").isMissing()) {
            String id = session.get("id").value();
            transactions = transactionService.getTransactionsByInvestor(id);
        }

        if (transactions == null) {
            ctx.sendRedirect("/login");
            return null;
        }
        return transactions;
    }
}
