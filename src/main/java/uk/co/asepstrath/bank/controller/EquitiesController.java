package uk.co.asepstrath.bank.controller;


import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.service.EquityService;

import java.util.List;

@Path("/equities")
public class EquitiesController {

    private EquityService equityService;

    public EquitiesController (EquityService equityService) {
        this.equityService = equityService;
    }

    @GET
    public List<Equity> home(@QueryParam String id) {
        return equityService.getAll();
    }
}
