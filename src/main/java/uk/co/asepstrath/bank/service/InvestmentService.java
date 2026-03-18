package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.controller.EquitiesController;
import uk.co.asepstrath.bank.controller.EtfController;
import uk.co.asepstrath.bank.controller.PricesController;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.util.List;

public class InvestmentService {
    private EquityService equityService;
    private EquitiesController equitiesController;
    private EtfService etfService;
    private EtfController etfController;
    private PriceService priceService;
    private PricesController pricesController;

    public Equity getEquity(String ticker){
        EquitiesController equitiesController = new EquitiesController();
        List<Equity> equityList = equitiesController.home("id");
        EquityService equityService = new EquityService();
        return equityService.getByTicker(ticker, equityList);
    }

    public Etf getEtf(String ticker){
        EtfController etfController = new EtfController();
        List<Etf> etfList = etfController.Etfs();
        EtfService etfService = new EtfService();
        return etfService.getByTicker(ticker, etfList);
    }
}
