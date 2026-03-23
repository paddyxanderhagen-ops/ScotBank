package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;

import java.util.List;

public class InvestmentService {
    private EquityService equityService;
    private EtfService etfService;
    public InvestmentService(EquityService equityService, EtfService etfService) {
        this.equityService = equityService;
        this.etfService = etfService;
    }

    public Equity getEquity(String ticker){
        return equityService.getEquity(ticker);
    }

    public Etf getEtf(String ticker){
        return etfService.getEtf(ticker);
    }
}
