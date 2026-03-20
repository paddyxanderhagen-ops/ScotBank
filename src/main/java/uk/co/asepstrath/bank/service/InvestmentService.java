package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.controller.EquitiesController;
import uk.co.asepstrath.bank.controller.EtfController;
import uk.co.asepstrath.bank.controller.PricesController;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.repository.EquityRepository;
import uk.co.asepstrath.bank.repository.TransactionRepository;

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
