package uk.co.asepstrath.bank.controller;


import com.google.gson.Gson;
import io.jooby.annotation.*;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.EquitiesDTO;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.service.EquityService;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.List;

@Path("/equities")
public class EquitiesController {
    @GET("/equity")
    public Equity Equity(@QueryParam String ticker) {
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/equities").accept("application/json").asString().getBody();

        EquitiesDTO equityResponse = gson.fromJson(response, EquitiesDTO.class);

        List<Equity> equityList = equityResponse.getEquities();

        EquityService equityService = new EquityService();

        return equityService.getByTicker(ticker, equityList);
    }

    @GET
    public List<Equity> home (@QueryParam String id) {
        Gson gson = new Gson();
        String response =  Unirest.get("https://api.asep-strath.co.uk/api/equities").accept("application/json").asString().getBody();

        EquitiesDTO equityResponse = gson.fromJson(response, EquitiesDTO.class);

        List<Equity> equityList = equityResponse.getEquities();

        return equityList;
    }
}
