package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;

import java.util.List;

public abstract class InvestorService {

    private InvestorRepository repo;

    public InvestorService (InvestorRepository repo) {
        this.repo = repo;
    }

    public Investor getInvestor (String id) {
        return repo.getInvestor(id);
    }

    public abstract List<Investor> getAll();
}
