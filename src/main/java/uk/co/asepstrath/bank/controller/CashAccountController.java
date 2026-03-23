package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.annotation.FormParam;
import io.jooby.annotation.POST;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import javax.sql.DataSource;

public class CashAccountController {
    private final InvestorService investorService;
    private final DataSource ds;
    private CashAccountImplementation account;
    private final TransactionService transactionService;

    public CashAccountController(InvestorService investorService, DataSource ds, TransactionService transactionService) {
        this.investorService = investorService;
        this.transactionService = transactionService;
        this.ds = ds;
    }

    @POST("/dashboard/transaction/deposit")
    public void deposit(@FormParam double amount, Context ctx) {
        String id = transaction(ctx);
        if (account != null) {
            account.deposit(amount);
            transactionService.createTransaction(id, "DEPOSIT", amount);
        }
    }

    @POST("/dashboard/transaction/withdraw")
    public void withdraw(@FormParam double amount, Context ctx) {
        String id = transaction(ctx);
        if (account != null) {
            account.withdraw(amount);
            transactionService.createTransaction(id, "WITHDRAWAL", amount);
        }
    }

    private String transaction(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String id = ctx.session().get("id").value();
        Investor investor = investorService.getInvestor(id);
        account = new CashAccountImplementation(investor, ds);
        return id;
    }
}

