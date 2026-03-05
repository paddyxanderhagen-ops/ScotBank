package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.Session;
import io.jooby.StatusCode;
import io.jooby.annotation.FormParam;
import io.jooby.annotation.POST;
import io.jooby.annotation.PathParam;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

public class CashAccountController {
    private final InvestorService investorService;

    public CashAccountController (InvestorService investorService) {
        this.investorService = investorService;
        this.investorService.getAll();
    }

    @POST("/dashboard/deposit")
    public void deposit(@FormParam double amount, Context ctx) {
        transaction(ctx).deposit(amount);
    }

    @POST("/dashboard/withdraw")
    public void withdraw(@FormParam double amount, Context ctx) {
        transaction(ctx).withdraw(amount);
    }

    private CashAccountImplementation transaction (Context ctx) {
        Session session = ctx.session();
        String id = session.get("id").value();
        Investor investor = investorService.getInvestor(id);

        return new CashAccountImplementation(investor);
    }
}

