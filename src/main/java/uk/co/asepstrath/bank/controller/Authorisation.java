package uk.co.asepstrath.bank.controller;

import uk.co.asepstrath.bank.model.Investor;

public class Authorisation {
    private final Investor investor;

    public Authorisation(Investor investor) {
        this.investor = investor;
    }

    public boolean isAuthorised() {
        return investor != null;
    }
}
