package uk.co.asepstrath.bank.controller;

import uk.co.asepstrath.bank.model.Investor;

public class Authorisation {
    private Investor investor;

    public Authorisation (Investor investor) {
        this.investor = investor;
    }

    public boolean isAuthorised () {
        if (investor == null) {
            return false;
        }

        return true;
    }
}
