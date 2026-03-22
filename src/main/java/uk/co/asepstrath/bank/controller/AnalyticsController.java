package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.service.AnalyticsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;
    private Authorisation authorisation;

    public AnalyticsController(AnalyticsService analyticsService, Authorisation authorisation) {
        this.analyticsService = analyticsService;
        this.authorisation = authorisation;
    }


    @GET
    public ModelAndView<Map<String,Object>> positions(Context ctx) {
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

        List<Position> positions =
                analyticsService.getPositions(id);

        System.out.println("analytics");

        for (Position position : positions) {
            System.out.println(position);
        }


        Map<String,Object> map = new HashMap<>();
        map.put("positions", positions);

        return new ModelAndView<>("positions.hbs", map);
    }
    @GET("/{ticker}")
    public ModelAndView<Map<String,Object>> position(Context ctx, @PathParam String ticker) {
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

        String investorId = ctx.session().get("id").value();
        Position position = analyticsService.getPosition(ticker, investorId);
        Map<String,Object> map = new HashMap<>();
        map.put("position", position);
        return new ModelAndView<>("position.hbs",map);
    }

}

