package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.dto.PriceDTO;
import uk.co.asepstrath.bank.service.PriceService;

import java.math.BigDecimal;
import java.util.List;

@Path("/prices")
public class PricesController {
    @GET("/price")
    BigDecimal Price(@QueryParam String ticker, @QueryParam String timeStamp) {
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/prices/".concat(ticker)).asString().getBody();

        PriceDTO priceResponse = gson.fromJson(response, PriceDTO.class);

        List<Price> priceList = priceResponse.getPriceList();

        PriceService priceService = new PriceService();

        return priceService.getByTimestamp(timeStamp,priceList).getPrice();
    }

    @GET
    List<Price> Prices(@QueryParam String ticker){
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/prices/"+ticker).asString().getBody();
        System.out.println(response);

        PriceDTO priceResponse = gson.fromJson(response, PriceDTO.class);

        return priceResponse.getPriceList();
    }
}
