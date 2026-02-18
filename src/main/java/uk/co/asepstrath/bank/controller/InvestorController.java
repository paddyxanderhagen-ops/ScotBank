package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.annotation.*;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.List;

@Path("/")
public class InvestorController {

    @GET("/")
    public String home (@QueryParam String id) {
        Gson gson = new Gson();
        String response =  Unirest.get("https://api.asep-strath.co.uk/api/investors").accept("application/json").asString().getBody();

        InvestorDTO investorResponse = gson.fromJson(response, InvestorDTO.class);

        List<Investor> investorList = investorResponse.getInvestors();

        InvestorService investorService = new InvestorService();
        return investorService.getById(id, investorList);
    }
}

