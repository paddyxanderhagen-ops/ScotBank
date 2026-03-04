package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Etf;

import java.util.List;

public class EtfService {
    public String getByTicker(String ticker, List<Etf> Etfs){
        for (Etf etf : Etfs) {
            if (etf.getTicker().equals(ticker)) {
                return "Name: " + etf.getTicker() + " Description: " + etf.getDescription();
            }
        }
        return "Etf:" + ticker + " not found";
    }
}
