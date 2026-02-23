package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.StatusCode;
import io.jooby.annotation.*;
import uk.co.asepstrath.bank.implementation.CashAccount;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.Map;

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

        CashAccount account = new CashAccount(investor);

        account.deposit(amount);
    }

    @POST("/{id}/withdraw")
    public void withdraw(@PathParam String id, @FormParam double amount, Context ctx) {
        if (id == null) {
            ctx.setResponseCode(StatusCode.FORBIDDEN);
        }

        Investor investor = investorService.getInvestor(id);

        CashAccount account = new CashAccount(investor);

        account.withdraw(amount);
    }
}

