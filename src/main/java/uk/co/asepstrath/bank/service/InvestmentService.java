package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.controller.EtfController;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;

import java.util.List;

public class InvestmentService {
    private EquityService equityService;
    public InvestmentService(EquityService equityService) {
        this.equityService = equityService;
    }

    public Equity getEquity(String ticker){;
        return equityService.getEquity(ticker);
    }

    public Etf getEtf(String ticker){
        EtfController etfController = new EtfController();
        List<Etf> etfList = etfController.Etfs();
        EtfService etfService = new EtfService();
        return etfService.getByTicker(ticker, etfList);
    }
}
