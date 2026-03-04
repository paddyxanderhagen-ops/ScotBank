package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;

import java.util.List;

public class InvestorService {

    private List<Investor> investors = null;

    public  Investor getInvestor (String id) {
        for (Investor investor : investors) {
            if (investor.getId().equals(id)) {
                return investor;
            }
        }
        return null;
    }

    public List<Investor> getAll () {
        Gson gson = new Gson();
        String response =  Unirest.get("https://api.asep-strath.co.uk/api/investors").accept("application/json").asString().getBody();

        InvestorDTO investorResponse = gson.fromJson(response, InvestorDTO.class);

        return investorResponse.getInvestors();
    }
}

