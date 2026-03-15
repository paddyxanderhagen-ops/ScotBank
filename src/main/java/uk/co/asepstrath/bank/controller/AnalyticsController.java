package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.service.AnalyticsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }


    @GET
    public ModelAndView<Map<String,Object>> positions(Context ctx) {

        String investorId = ctx.session().get("id").value();
        List<Position> positions =
                analyticsService.getPositions(investorId);


        Map<String,Object> map = new HashMap<>();
        map.put("positions", positions);

        return new ModelAndView<>("positions.hbs", map);
    }
    @GET("/{ticker}")
    public ModelAndView<Map<String,Object>> position(Context context, @PathParam String ticker) {
        String investorId = context.session().get("id").value();
        Position position = analyticsService.getPosition(ticker, investorId);
        Map<String,Object> map = new HashMap<>();
        map.put("position", position);
        return new ModelAndView<>("position.hbs",map);
    }

}

