package uk.co.asepstrath.bank.model.dto;

import uk.co.asepstrath.bank.model.Investor;

import java.util.ArrayList;
import java.util.List;

public class InvestorDTO {
    private final List<Investor> investors;

    public InvestorDTO() {
        investors = new ArrayList<>();
    }

    public List<Investor> getInvestors() {
        return this.investors;
    }

    public void addInvestor(Investor investor) {
        this.investors.add(investor);
    }
}
