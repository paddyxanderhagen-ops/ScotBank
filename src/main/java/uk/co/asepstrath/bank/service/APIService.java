package uk.co.asepstrath.bank.service;

import com.google.gson.Gson;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

public class APIService {

    private StringBuilder url;
    private Gson gson;

    public APIService () {
        url = new StringBuilder("https://api.asep-strath.co.uk/api/");
        gson = new Gson();
    }

    public InvestorDTO getAllInvestor () {
        url.append("investors");
        String response =  Unirest.get(url.toString()).accept("application/json").asString().getBody();

        InvestorDTO investorDTO = gson.fromJson(response, InvestorDTO.class);
        int i = url.indexOf("investors");
        url.delete(i, url.length());
        return investorDTO;
    }

    public TransactionDTO getAllTransaction () {
        url.append("transactions");
        String response = Unirest.get(url.toString()).accept("application/json").asString().getBody();

        TransactionDTO transactionDTO = gson.fromJson(response, TransactionDTO.class);
        int i = url.indexOf("transactions");
        url.delete(i, url.length());
        return transactionDTO;
    }
}
