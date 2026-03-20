package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.annotation.FormParam;
import io.jooby.annotation.POST;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import javax.sql.DataSource;

public class CashAccountController {
    private final InvestorService investorService;
    private CashAccountImplementation account;
    private final DataSource ds;

    public CashAccountController(InvestorService investorService, DataSource ds) {
        this.investorService = investorService;
        this.ds = ds;
    }

    @POST("/dashboard/deposit")
    public void deposit(@FormParam double amount, Context ctx) {
        transaction(ctx);
        if (account != null) {
            account.deposit(amount);
        }
    }

    @POST("/dashboard/withdraw")
    public void withdraw(@FormParam double amount, Context ctx) {
        transaction(ctx);
        if (account != null) {
            account.withdraw(amount);
        }
    }

    private void transaction(Context ctx) {
        if (ctx == null) {
            return;
        }
        String id = ctx.session().get("id").value();
        Investor investor = investorService.getInvestor(id);
        account = new CashAccountImplementation(investor, ds);
    }
}

