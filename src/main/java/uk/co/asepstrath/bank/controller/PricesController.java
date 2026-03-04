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

@Path("prices")
public class PricesController {
    @GET
    BigDecimal Prices(@QueryParam String ticker, @QueryParam String timeStamp) {
        Gson gson = new Gson();
        String response = Unirest.get("https://api.asep-strath.co.uk/api/prices/"+ticker).asString().getBody();

        PriceDTO priceResponse = gson.fromJson(response, PriceDTO.class);

        List<Price> priceList = priceResponse.getPriceList();

        PriceService priceService = new PriceService();

        priceService.getByTimestamp(timeStamp, priceList);
    }
}
