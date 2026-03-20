package uk.co.asepstrath.bank.controller;

import io.jooby.ModelAndView;
import io.jooby.annotation.*;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.service.EquityService;
import uk.co.asepstrath.bank.service.InvestmentService;
import uk.co.asepstrath.bank.service.PriceService;
import java.util.HashMap;
import java.util.Map;

@Path("/Investments")
public class InvestmentController {

    private PriceService priceService;
    private EquityService equityService;

    public InvestmentController(EquityService equityService, PriceService priceService) {
        this.equityService = equityService;
        this.priceService = priceService;
    }

    @GET("")
    public ModelAndView<Map<String, Object>> investmentPage () {
        return new ModelAndView<>("investments.hbs", new HashMap<>());
    }

    @GET("/Search")
    public ModelAndView<Map<String, Object>> investmentPage (@QueryParam String id) {
        InvestmentService investmentService = new InvestmentService(equityService);
        ModelAndView<Map<String, Object>> model;
            try {
                Equity equity = investmentService.getEquity(id);
                Map<String, Object> investment = new HashMap<>();
                investment.put("equity", equity);
                try {
                    investment.put("price", priceService.getLatestPrice(id));
                }
                    catch (Exception e1) {
                    investment.put("price", "price not avialable");
                }
                model = new ModelAndView<>("EquityInvestment.hbs", investment);
            }
            catch (Exception e) {
                Etf etf = investmentService.getEtf(id);
                if (etf == null) {
                    Map<String, Object> modelMap = new HashMap<>();
                    model = new ModelAndView<>("investmentNotFound.hbs", modelMap);
                } else {
                    Map<String, Object> investment = new HashMap<>();
                    //Etf always has price not available
                    //because price Repository never reads in prices for etfs
                    investment.put("etf", etf);
                    try {
                        investment.put("price", priceService.getLatestPrice(id));
                    }
                    catch (Exception e1) {
                        investment.put("price", "price not avialable");
                    }
                    model = new ModelAndView<>("EtfInvestment.hbs", investment);
                }
            }
        return model;
    }
}
