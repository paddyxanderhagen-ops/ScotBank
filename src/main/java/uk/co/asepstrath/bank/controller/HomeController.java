package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.StatusCode;
import io.jooby.annotation.GET;
import io.jooby.annotation.HeaderParam;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HomeController {

    private final InvestorService investorService;
    private Authorisation authorisation;

    public HomeController (InvestorService investorService) {
        this.investorService = investorService;
    }

    @GET("/")
    public ModelAndView<Map<String, Object>> landingPage (@PathParam String id, Context ctx) {
        Session session = ctx.session();
        session.put("id", "");
        if (id == null) {
            return new ModelAndView<>("landingPage.hbs", new HashMap<>());
        }
        return null;
    }

    @GET("/dashboard")
    public ModelAndView<Map<String, Object>> dashboardPage (Context ctx) {
        Session session = ctx.session();
        String id = null;
        if (!session.get("id").isMissing()) {
            id = session.get("id").value();
        }

        Investor investor = investorService.getInvestor(id);
        authorisation = new Authorisation(investor);

        if (!authorisation.isAuthorised()) {
            ctx.sendRedirect("/login");
        }

        CashAccountImplementation account = new CashAccountImplementation(investor);

        Map<String, Object> map = new HashMap<>();
        map.put("name", investor.getName());
        map.put("balance", Math.floor(account.getBalance() * 100.0) / 100.0);

        return new ModelAndView<>("index.hbs", map);
    }
}
