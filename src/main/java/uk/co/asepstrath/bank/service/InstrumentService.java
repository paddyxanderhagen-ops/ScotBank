package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Instrument;
import uk.co.asepstrath.bank.repository.InstrumentRepository;

import java.sql.SQLException;
import java.util.List;

public class InstrumentService {

    private final InstrumentRepository repo;

    public InstrumentService(InstrumentRepository repo) {
        this.repo = repo;
    }

    public Instrument getInstrument(String ticker) {
        try {
            Instrument instrument = repo.getInstrument(ticker);

            if (instrument == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Instrument " + ticker + " not found");
            }

            return instrument;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve instrument " + ticker + " " + e);
        }
    }

    public List<Instrument> getAll() {
        try {
            List<Instrument> instruments = repo.getAll();

            if (instruments.isEmpty()) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Instruments not found");
            }

            return instruments;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve instruments");
        }
    }

}
