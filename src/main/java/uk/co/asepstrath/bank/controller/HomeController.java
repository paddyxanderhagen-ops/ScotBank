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

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HomeController {

    private final InvestorService investorService;
    private Authorisation authorisation;
    private DataSource ds;

    public HomeController (InvestorService investorService, DataSource ds) {
        this.investorService = investorService;
        this.ds = ds;
    }

    @GET("/")
    public ModelAndView<Map<String, Object>> landingPage (@PathParam String id, Context ctx) {
        if (ctx != null) {
            Session session = ctx.session();
            session.put("id", "");
        }
        if (id == null || id.isEmpty()) {
            return new ModelAndView<>("landingPage.hbs", new HashMap<>());
        }
        return null;
    }

    @GET("/dashboard")
    public ModelAndView<Map<String, Object>> dashboardPage (Context ctx) {
        Session session = null;
        String id = null;
        if (ctx != null) {
            session = ctx.session();
        }
        if (session != null && !session.get("id").isMissing()) {
            id = session.get("id").value();
        }

        Investor investor = investorService.getInvestor(id);
        authorisation = new Authorisation(investor);

        if (!authorisation.isAuthorised()) {
            ctx.sendRedirect("/login");
        }

        CashAccountImplementation account = new CashAccountImplementation(investor, ds);

        Map<String, Object> map = new HashMap<>();
        map.put("name", investor.getName());
        map.put("balance", Math.floor(account.getBalance() * 100.0) / 100.0);

        return new ModelAndView<>("index.hbs", map);
    }
}
