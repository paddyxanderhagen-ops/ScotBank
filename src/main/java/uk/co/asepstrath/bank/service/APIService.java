package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

public class APIService {

    private String base_url;
    private Gson gson;

    public APIService () {
        base_url = "https://api.asep-strath.co.uk/api/";
        gson = new Gson();
    }

    public InvestorDTO getAllInvestor () {
        String url = base_url + "investors";
        String response =  Unirest.get(url).accept("application/json").asString().getBody();

        InvestorDTO investorDTO = gson.fromJson(response, InvestorDTO.class);

        return investorDTO;
    }

    public TransactionDTO getAllTransaction () {
        String url = base_url + "transactions";
        String response = Unirest.get(url).accept("application/json").asString().getBody();

        TransactionDTO transactionDTO = gson.fromJson(response, TransactionDTO.class);

        return transactionDTO;
    }
}
