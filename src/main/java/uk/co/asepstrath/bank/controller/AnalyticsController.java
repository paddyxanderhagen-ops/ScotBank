package uk.co.asepstrath.bank.controller;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.service.AnalyticsService;

import java.util.List;

@Path("/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GET("/positions")
    public List<Position> positions(@QueryParam String investorId) {
        return analyticsService.getPositions(investorId);
    }
}

