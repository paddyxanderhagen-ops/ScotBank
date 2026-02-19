package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.annotation.*;
import uk.co.asepstrath.bank.implementation.CashAccount;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;


import java.util.HashMap;
import java.util.Map;

@Path("/")
public class InvestorController {

    @GET("/")
    public ModelAndView<Map<String, Object>> home (@QueryParam String id, Context ctx) {
        InvestorService investorService = new InvestorService();
        investorService.getAll(); // probably best option for now until we can query the
        Investor investor = investorService.getInvestor(id);

        CashAccount account = new CashAccount();

        Map<String, Object> map = new HashMap<>();
        map.put("name", investor.getName());
        map.put("balance", account.getBalance());

        account.deposit((double) 200.0F);
        account.withdraw((double) 50.0F);
        account.getBalance();



        return new ModelAndView<>("index.hbs", map);
    }
}

