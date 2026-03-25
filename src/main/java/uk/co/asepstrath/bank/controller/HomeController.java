package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
public class HomeController {

    private final InvestorService investorService;
    private final TransactionService transactionService;

    public HomeController(InvestorService investorService, TransactionService transactionService) {
        this.investorService = investorService;
        this.transactionService = transactionService;
    }

    @GET("/")
    public ModelAndView<Map<String, Object>> landingPage(Context ctx) {
        if (ctx != null) {
            Session session = ctx.session();
            session.put("id", "");
        }
        return new ModelAndView<>("landing.hbs", new HashMap<>());
    }

    @GET("/contact")
    public ModelAndView<Map<String, Object>> contact() {
        return new ModelAndView<>("contact.hbs", new HashMap<>());
    }

    @GET("/dashboard")
    public ModelAndView<Map<String, Object>> dashboardPage(Context ctx) {
        Investor investor = getSession(ctx);
        if (investor == null) {
            return null;
        }

        List<Transaction> transactions = transactionService.getTransactionsByInvestor(investor.getId()).reversed().stream().limit(5).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("id", investor.getId());
        map.put("name", investor.getName());
        map.put("balance", Math.floor(investor.getCashBalance() * 100.0) / 100.0);
        map.put("transactions", transactions);

        return new ModelAndView<>("index.hbs", map);
    }

    @GET("/cash-management")
    public ModelAndView<Map<String, Object>> cashManagement(Context ctx) {
        Investor investor = getSession(ctx);
        if (investor == null) {
            return null;
        }

        Map<String, Object> model = new HashMap<>();

        model.put("name", investor.getName());
        model.put("balance", investor.getCashBalance());

        return new ModelAndView<>("cash-management.hbs", model);
    }

    private Investor getSession(Context ctx) {
        if (ctx == null) {
            return null;
        }

        Session session = ctx.session();

        Investor investor = null;

        if (!session.get("id").isMissing()) {
            String id = session.get("id").value();
            investor = investorService.getInvestor(id);
        }

        if (investor == null) {
            ctx.sendRedirect("/login");
            return null;
        }
        return investor;
    }
}
