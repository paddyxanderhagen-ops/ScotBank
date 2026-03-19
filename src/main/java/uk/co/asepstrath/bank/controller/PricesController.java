package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.PriceDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.service.PriceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/prices")
public class PricesController {

    private StringBuilder url = new StringBuilder("https://api.asep-strath.co.uk/api/");
    private TransactionDTO transactionDTO;
    private List<Transaction> transactions = new ArrayList<>();
    private Gson gson = new Gson();
    private final Map<String, List<Price>> cache = new HashMap<>();

//    @GET("/price")
//    public BigDecimal Price(@QueryParam String ticker, @QueryParam String timeStamp) {
//        Gson gson = new Gson();
//        String response = Unirest.get("https://api.asep-strath.co.uk/api/prices/".concat(ticker)).asString().getBody();
//
//        PriceDTO priceResponse = gson.fromJson(response, PriceDTO.class);
//
//        List<Price> priceList = priceResponse.getPriceList();
//
//        PriceService priceService = new PriceService();
//
//        return priceService.getByTimestamp(timeStamp,priceList).getPrice();
//    }
//
//    public List<Price> Prices(String ticker) {
//        if (cache.containsKey(ticker)) {
//            return cache.get(ticker);
//        }
//
//        System.out.println("Loading all prices for: " + ticker);
//        String nextToken = null;
//        List<Price> prices = new ArrayList<>();
//
//        do {
//            StringBuilder requestUrl = new StringBuilder("https://api.asep-strath.co.uk/api/prices/")
//                    .append(ticker);
//
//            if (nextToken != null) {
//                requestUrl.append("?nextToken=").append(nextToken);
//            }
//
//            String response = Unirest.get(requestUrl.toString()).accept("application/json").asString().getBody();
//            PriceDTO priceDTO = gson.fromJson(response, PriceDTO.class);
//
//            if (priceDTO == null || priceDTO.getPriceList() == null) {
//                break;
//            }
//
//            prices.addAll(priceDTO.getPriceList());
//            nextToken = (priceDTO.getPages() != null) ? priceDTO.getPages().getNextToken() : null;
//
//        } while (nextToken != null);
//
//        System.out.println("Loaded " + prices.size() + " prices for " + ticker);
//        cache.put(ticker, prices);
//        return prices;
//    }
//
//    public BigDecimal getCurrentPrice(String ticker) {
//        List<Price> prices = Prices(ticker);
//        PriceService priceService = new PriceService();
//        return priceService.getLatestPrice(prices);
//    }
//
//
//        gson = new Gson();
//        String response = Unirest.get("https://api.asep-strath.co.uk/api/prices/"+ticker).asString().getBody();
//
//        PriceDTO priceResponse = gson.fromJson(response, PriceDTO.class);
//
//        return priceResponse.getPriceList();

}
