package uk.co.asepstrath.bank.service;

import uk.co.asepstrath.bank.model.Investor;

import java.util.List;

public class InvestorService {
    public String getById (String id, List<Investor> investorList) {
        for (Investor investor : investorList) {
            if (investor.getId().equals(id)) {
                return "Name: " + investor.getName() + " Balance: " + investor.getCashBalance();
            }
        }
        return "Investor:" + id + " not found";
    }
}
