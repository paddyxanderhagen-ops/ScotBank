package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HomeController {

    private final InvestorService investorService;
    private final TransactionService transactionService;

    public HomeController (InvestorService investorService, TransactionService transactionService) {
        this.investorService = investorService;
        this.transactionService = transactionService;
    }

    @GET("/")
    public ModelAndView<Map<String, Object>> homePage (@PathParam String id) {
        if (id == null) {
            return new ModelAndView<>("unauthorised.hbs", new HashMap<>());
        }
        return null;
    }

    @GET("/{id}")
    public ModelAndView<Map<String, Object>> userHome (@PathParam String id, Context ctx) {
        Investor investor = investorService.getInvestor(id);
        if (investor == null) {
            ctx.setResponseCode(StatusCode.UNAUTHORIZED);
            return new ModelAndView<>("unauthorised.hbs", new HashMap<>());
        }

        CashAccountImplementation account = new CashAccountImplementation(investor);

        Map<String, Object> map = new HashMap<>();
        map.put("name", investor.getName());
        map.put("balance", Math.floor(account.getBalance() * 100.0) / 100.0);

        return new ModelAndView<>("index.hbs", map);
    }
}
