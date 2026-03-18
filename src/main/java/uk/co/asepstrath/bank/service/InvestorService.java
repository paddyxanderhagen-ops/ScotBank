package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;

import java.sql.SQLException;

public class InvestorService {

    private InvestorRepository repo;

    public InvestorService (InvestorRepository repo) {
        this.repo = repo;
    }

    public Investor getInvestor (String id) {
        try {
            Investor investor = repo.getInvestor(id);
            if (investor == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Investor not found: " + id);
            }
            return investor;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve investor: " + id);
        }
    }
}
