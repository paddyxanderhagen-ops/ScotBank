package uk.co.asepstrath.bank.controller;

import ch.qos.logback.core.model.Model;
import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.StatusCode;
import io.jooby.annotation.FormParam;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.HashMap;
import java.util.Map;


@Path("/login")
public class LoginController {

    private InvestorService investorService;

    public LoginController (InvestorService investorService) {
        this.investorService = investorService;
    }

    @GET("")
    public ModelAndView<Map<String, Object>> loginPage() {
        return new ModelAndView<>("login.hbs", new HashMap<>());
    }

    @POST("")
    public ModelAndView<Map<String, Object>> postLogin(@FormParam String id, Context ctx) {
        if (id == null) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Please enter an ID");
            return new ModelAndView<>("login.hbs", model);
        }

        Investor investor = investorService.getInvestor(id);
        if (investor == null || id.isBlank()) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Investor not found");
            return new ModelAndView<>("login.hbs", model);
        }

        Session session = ctx.session();
        session.put("id", id);
        ctx.sendRedirect("/dashboard");
        return null;
    }

}
