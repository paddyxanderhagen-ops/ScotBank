package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.repository.InvestorRepository;

import java.util.ArrayList;
import java.util.List;

public class InvestorService {

    private List<Investor> investors;

    public InvestorService () {
        investors = new ArrayList<>();
    }

    public InvestorService (InvestorRepository investorRepo) {
        investors = investorRepo.getAll().getInvestors();
    }

    public  Investor getInvestor (String id) {
        for (Investor investor : investors) {
            if (investor.getId().equals(id)) {
                return investor;
            }
        }
        return null;
    }

    public List<Investor> getAll () {
        return investors;
    }
}
