package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.repository.EtfRepository;

import java.sql.SQLException;
import java.util.List;

public class EtfService {
    private final EtfRepository repo;

    public EtfService(EtfRepository repo) {
        this.repo = repo;
    }

    public Etf getEtf(String ticker) {
        try {
            Etf etf = repo.getEtf(ticker);

            if (etf == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "ETF not found");
            }

            return etf;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve ETF");
        }
    }

    public List<Etf> getAll() {
        try {
            List<Etf> etfs = repo.getAll();

            if (etfs.isEmpty()) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "ETFs not found");
            }

            return etfs;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve ETF");
        }
    }
}