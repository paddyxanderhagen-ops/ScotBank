package uk.co.asepstrath.bank.controller;


import io.jooby.Context;
import io.jooby.Session;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.service.EquityService;

import java.util.List;

@Path("/equities")
public class EquitiesController {

    private EquityService equityService;
    private Authorisation authorisation;

    public EquitiesController (EquityService equityService, Authorisation authorisation) {
        this.equityService = equityService;
        this.authorisation = authorisation;
    }

    @GET
    public List<Equity> home(Context ctx) {
        String id = null;
        Session session = null;
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

        return equityService.getAll();
    }
}
