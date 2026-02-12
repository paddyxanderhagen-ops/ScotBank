package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Investor;

import java.util.List;

public class InvestorDTO {
    private List<Investor> investors;

    public List<Investor> getInvestors () {
        return this.investors;
    }
}
