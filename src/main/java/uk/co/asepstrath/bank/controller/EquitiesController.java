package uk.co.asepstrath.bank.controller;


import com.google.gson.Gson;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.dto.EquitiesDTO;
import uk.co.asepstrath.bank.model.dto.EtfDTO;
import uk.co.asepstrath.bank.service.EquityService;
import uk.co.asepstrath.bank.service.EtfService;

import java.util.List;

@Path("/equities")
public class EquitiesController {
    @GET
    String Equity(@QueryParam String ticker) {
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/equities").accept("application/json").asString().getBody();

        EquitiesDTO equityResponse = gson.fromJson(response, EquitiesDTO.class);

        List<Equity> equityList = equityResponse.getEquities();

        EquityService equityService = new EquityService();

        return equityService.getByTicker(ticker, equityList);
    }
}
