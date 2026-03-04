package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Equity;

import java.util.List;

public class EquityService {
        public Equity getByTicker(String ticker, List<Equity> equityList){
            for(Equity equity : equityList){
                if(equity.getTicker().equals(ticker)){
                    return equity;
                }
            }
            return null;
        }

        public List<Equity> getAll(List<Equity> equityList){
            return equityList;
        }
}
