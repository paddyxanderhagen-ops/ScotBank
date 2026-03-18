package uk.co.asepstrath.bank.controller;


import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;

import java.util.HashMap;
import java.util.Map;

@Path("/portfolio-categorisation")
public class PortfolioCategorisationController {

    @GET
    public ModelAndView getPortfolioBreakdown() {
        Map<String, Object> model = new HashMap<>();

        // TODO: Add any necessary data to the model here
        // The script portfolio-breakdown.js should be included inside portfolio-breakdown.hbs template.

        return new ModelAndView("portfolio-categorisation.hbs", model);
    }
}