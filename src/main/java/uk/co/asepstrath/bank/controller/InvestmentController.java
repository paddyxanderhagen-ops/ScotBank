package uk.co.asepstrath.bank.controller;

import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.service.EquityService;
import uk.co.asepstrath.bank.service.EtfService;
import uk.co.asepstrath.bank.service.InvestmentService;
import uk.co.asepstrath.bank.service.PriceService;

import java.util.HashMap;
import java.util.Map;

@Path("/investments")
public class InvestmentController {

    private final PriceService priceService;
    private final EquityService equityService;
    private final EtfService etfService;

    public InvestmentController(EquityService equityService, PriceService priceService, EtfService etfService) {
        this.equityService = equityService;
        this.etfService = etfService;
        this.priceService = priceService;
    }

    @GET("")
    public ModelAndView<Map<String, Object>> investmentPage() {
        return new ModelAndView<>("investments.hbs", new HashMap<>());
    }

    @GET("/search")
    public ModelAndView<Map<String, Object>> investmentPage(@QueryParam String ticker) {
        InvestmentService investmentService = new InvestmentService(equityService, etfService);
        ModelAndView<Map<String, Object>> model;
        try {
            Equity equity = investmentService.getEquity(ticker);
            Map<String, Object> investment = new HashMap<>();
            investment.put("equity", equity);
            try {
                investment.put("price", priceService.getLatestPrice(ticker));
            } catch (Exception e1) {
                investment.put("price", "price not avialable");
            }
            model = new ModelAndView<>("EquityInvestment.hbs", investment);
        } catch (Exception e) {
            Etf etf = investmentService.getEtf(ticker);
            if (etf == null) {
                Map<String, Object> modelMap = new HashMap<>();
                model = new ModelAndView<>("investmentNotFound.hbs", modelMap);
            } else {
                Map<String, Object> investment = new HashMap<>();
                investment.put("etf", etf);
                try {
                    investment.put("price", priceService.getLatestPrice(ticker));
                } catch (Exception e1) {
                    investment.put("price", "price not avialable");
                }
                model = new ModelAndView<>("EtfInvestment.hbs", investment);
            }
        }
        return model;
    }
}
