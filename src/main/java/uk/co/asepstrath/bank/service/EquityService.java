package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.repository.EquitiesRepository;

import java.util.List;

public class EquityService {

    private EquitiesRepository repo;

    public EquityService (EquitiesRepository repo) {
        this.repo = repo;
    }

    public Equity getEquity(String ticker) {
        return null;
    }

    public List<Equity> getAll(List<Equity> equityList) {
        return equityList;
    }
}
