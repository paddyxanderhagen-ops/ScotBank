package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.repository.EquityRepository;

import java.sql.SQLException;
import java.util.List;

public class EquityService {

    private EquityRepository repo;

    public EquityService (EquityRepository repo) {
        this.repo = repo;
    }

    public Equity getEquity(String ticker) {
        try {
            Equity equity = repo.getEquity(ticker);

            if (equity == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Equity not found");
            }

            return equity;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve equity");
        }
    }

    public List<Equity> getAll() {
        try {
            List<Equity> equities = repo.getAll();

            if (equities.isEmpty()) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Equities not found");
            }

            return equities;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve equities");
        }
    }
}
