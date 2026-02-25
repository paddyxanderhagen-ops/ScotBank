package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
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

    @POST("/{id}/deposit")
    public void deposit(@PathParam String id, @FormParam double amount, Context ctx) {
        if (id == null) {
            ctx.setResponseCode(StatusCode.FORBIDDEN);
        }

        Investor investor = investorService.getInvestor(id);

        CashAccountImplementation account = new CashAccountImplementation(investor);

        account.deposit(amount);
    }

    @POST("/{id}/withdraw")
    public void withdraw(@PathParam String id, @FormParam double amount, Context ctx) {
        if (id == null) {
            ctx.setResponseCode(StatusCode.FORBIDDEN);
        }

        Investor investor = investorService.getInvestor(id);

        CashAccountImplementation account = new CashAccountImplementation(investor);

        account.withdraw(amount);
    }
}

