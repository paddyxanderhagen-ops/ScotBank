package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;

import java.util.List;

public class InvestorService {

    private List<Investor> investors = null;
    private InvestorDTO investorDTO;

    public InvestorService () {}

    public InvestorService (InvestorDTO apiResponse) {
        this.investorDTO = apiResponse;
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
        investors = investorDTO.getInvestors();
        return investors;
    }
}
