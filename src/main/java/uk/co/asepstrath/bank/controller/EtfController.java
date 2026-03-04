package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.EtfDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.service.EtfService;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.List;

@Path("/etfs")
public class EtfController {
    @GET
    Etf Etf(@QueryParam String ticker) {
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/etfs").accept("application/json").asString().getBody();

        EtfDTO transactionResponse = gson.fromJson(response, EtfDTO.class);

        List<Etf> etfList = transactionResponse.getEtfs();

        EtfService etfService = new EtfService();

        return etfService.getByTicker(ticker, etfList);
    }

    @GET
    List<Etf> Etfs(){
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/etfs").accept("application/json").asString().getBody();

        EtfDTO transactionResponse = gson.fromJson(response, EtfDTO.class);

        return transactionResponse.getEtfs();
    }
}
