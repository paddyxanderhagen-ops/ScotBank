package uk.co.asepstrath.bank.controller;

import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.service.InvestorService;

public class Authorisation {

    private InvestorRepository repo;

    public Authorisation(InvestorRepository repo) {
        this.repo = repo;
    }

    public boolean isAuthorised(String id) {
        InvestorService investorService = new InvestorService(repo);
        Investor investor = investorService.getInvestor(id);
        return investor != null;
    }
}
