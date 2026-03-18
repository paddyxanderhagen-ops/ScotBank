package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.*;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.service.EquityService;
import uk.co.asepstrath.bank.service.EtfService;
import uk.co.asepstrath.bank.service.InvestmentService;

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
    public ModelAndView investmentPage (@QueryParam String id) {
        InvestmentService investmentService = new InvestmentService();
        Equity equity = investmentService.getEquity(id);
        ModelAndView model;
        if (equity == null) {
            Etf etf = investmentService.getEtf(id);
            if (etf == null) {
                model = new ModelAndView<>("investmentNotFound.hbs", "error");
            } else {
                Map<String, Object> investment = new HashMap<>();
                investment.put("etf", etf);
                model = new ModelAndView<>("EtfInvestment.hbs", investment);
            }
        } else {
            Map<String, Object> investment = new HashMap<>();
            investment.put("equity", equity);
             model = new ModelAndView<>("EquityInvestment.hbs", investment);
        }
        return model;
    }
}
