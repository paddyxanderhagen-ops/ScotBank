package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.*;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.service.EquityService;
import uk.co.asepstrath.bank.service.EtfService;
import uk.co.asepstrath.bank.service.InvestmentService;
import uk.co.asepstrath.bank.service.PriceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/Investments")
public class InvestmentController {
    @GET("")
    public ModelAndView<Map<String, Object>> investmentPage () {
        return new ModelAndView<>("investments.hbs", new HashMap<>());
    }

    @GET("/Search")
    public ModelAndView<Map<String, Object>> investmentPage (@QueryParam String id) {
        InvestmentService investmentService = new InvestmentService();
        Equity equity = investmentService.getEquity(id);
        ModelAndView<Map<String, Object>> model;
        if (equity == null) {
            Etf etf = investmentService.getEtf(id);
            if (etf == null) {
                Map<String, Object> modelMap = new HashMap<>();
                model = new ModelAndView<>("investmentNotFound.hbs", modelMap);
            } else {
                Map<String, Object> investment = new HashMap<>();
                investment.put("etf", etf);
                PricesController pricesController = new PricesController();
                List<Price> priceList = pricesController.Prices(id);
                PriceService priceService = new PriceService();
                investment.put("price", priceService.getLatestPrice(priceList));
                model = new ModelAndView<>("EtfInvestment.hbs", investment);
            }
        } else {
            Map<String, Object> investment = new HashMap<>();
            investment.put("equity", equity);
            PricesController pricesController = new PricesController();
            List<Price> priceList = pricesController.Prices(id);
            PriceService priceService = new PriceService();
            investment.put("price", priceService.getLatestPrice(priceList));
             model = new ModelAndView<>("EquityInvestment.hbs", investment);
        }
        return model;
    }
}
