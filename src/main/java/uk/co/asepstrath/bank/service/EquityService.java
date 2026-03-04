package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Equity;

import java.util.List;

public class EquityService {
        public String getByTicker(String ticker, List<Equity> equityList){
            for(Equity equity : equityList){
                if(equity.getTicker().equals(ticker)){
                    return "Name: " + equity.getName() + " Sector: " + equity.getSector();
                }
            }
            return "Ticker: " + ticker + " not found";
        }
}
