package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
public class HomeController {

    private final InvestorService investorService;
    private Authorisation authorisation;
    private final DataSource ds;
    private final TransactionService transactionService;

    public HomeController(InvestorService investorService, DataSource ds, TransactionService transactionService, Authorisation authorisation) {
        this.investorService = investorService;
        this.ds = ds;
        this.transactionService = transactionService;
        this.authorisation = authorisation;
    }

    @GET("/")
    public ModelAndView<Map<String, Object>> landingPage (Context ctx) {
        if (ctx != null) {
            Session session = ctx.session();
            session.put("id", "");
        }
        return new ModelAndView<>("landing.hbs", new HashMap<>());
    }

    @GET("/contact")
    public ModelAndView contact() {
        return new ModelAndView<>("contact.hbs", new HashMap<>());
    }

    @GET("/dashboard")
    public ModelAndView<Map<String, Object>> dashboardPage(Context ctx) throws SQLException {
        Session session = null;
        String id = null;
        if (ctx != null) {
            session = ctx.session();
        }
        if (session != null && !session.get("id").isMissing()) {
            id = session.get("id").value();
        }

        if (!authorisation.isAuthorised(id)) {
            ctx.sendRedirect("/login");
            return null;
        }

        Investor investor = investorService.getInvestor(id);


        CashAccountImplementation account = new CashAccountImplementation(investor, ds);

        List<Transaction> transactions = transactionService.getTransactionsByInvestor(investor.getId()).stream().limit(5).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("id", investor.getId());
        map.put("name", investor.getName());
        map.put("balance", Math.floor(account.getBalance() * 100.0) / 100.0);
        map.put("transactions", transactions);

        return new ModelAndView<>("index.hbs", map);
    }

    @GET("/cash-management")
    public ModelAndView<Map<String, Object>> cashManagement(Context ctx) {
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
            return null;
        }

        Map<String, Object> model = new HashMap<>();

        model.put("name", investor.getName());
        model.put("balance", investor.getCashBalance());

        return new ModelAndView<>("cash-management.hbs", model);
    }
}
