package uk.co.asepstrath.bank.repository;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Constituent;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.Instrument;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstrumentRepository {

    private final DataSource ds;

    public InstrumentRepository(DataSource ds) {
        this.ds = ds;
    }

    public void setUp() throws SQLException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS instrument (
                            ticker VARCHAR(20) PRIMARY KEY,
                            name VARCHAR(100),
                            type VARCHAR(10),
                    
                            sector VARCHAR(50),
                    
                            description VARCHAR(500),
                            inception_date VARCHAR(20)
                        )
                    """);

        }
    }

    public void saveAll(List<Equity> equities, List<Etf> etfs) throws SQLException {
        String sql = """
                MERGE INTO instrument (ticker, name, type, sector, description, inception_date)
                KEY(ticker)
                VALUES (?, ?, ?, ?, ?, ?)
                """;


        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (equities != null) {
                for (Equity equity : equities) {
                    ps.setString(1, equity.getTicker());
                    ps.setString(2, equity.getName());
                    ps.setString(3, "equity");
                    ps.setString(4, equity.getSector());
                    ps.setString(5, null);
                    ps.setString(6, null);
                    ps.addBatch();
                }
            }

            if (etfs != null) {
                for (Etf etf : etfs) {
                    ps.setString(1, etf.getTicker());
                    ps.setString(2, etf.getName());
                    ps.setString(3, "etf");
                    ps.setString(4, null);
                    ps.setString(5, etf.getDescription());
                    ps.setString(6, etf.getInceptionDate());
                    ps.addBatch();
                }
            }

            ps.executeBatch();
        }
    }

    public Instrument getInstrument(String ticker) throws SQLException {
        String sql = """
                SELECT i.*, c.equity_ticker as constituent_ticker, c.weight
                FROM instrument i
                LEFT JOIN constituents c 
                ON c.etf_ticker = i.ticker
                WHERE i.ticker = ?
                """;

        Instrument instrument = null;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ticker);

            ResultSet rs = ps.executeQuery();
            List<Constituent> constituents = new ArrayList<>();

            while (rs.next()) {
                String type = rs.getString("type");

                if (type.equalsIgnoreCase("equity")) {
                    instrument = new Equity(rs.getString("ticker"), rs.getString("name"), rs.getString("sector"));
                } else if (type.equalsIgnoreCase("etf")) {
                    constituents.add(new Constituent(rs.getString("constituent_ticker"), rs.getDouble("weight")));
                    instrument = new Etf(rs.getString("ticker"), rs.getString("name"), rs.getString("description"), rs.getString("inception_date"), constituents);
                } else {
                    throw new StatusCodeException(StatusCode.NOT_FOUND, "Error: Could not find type " + type);
                }
            }
        }

        return instrument;
    }

    public List<Instrument> getAll() throws SQLException {
        String sql = """
                SELECT i.*, c.equity_ticker as constituent_ticker, c.weight
                FROM instrument i
                LEFT JOIN constituents c ON c.etf_ticker = i.ticker         
                """;

        List<Instrument> instruments = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Instrument instrument = null;
                String type = rs.getString("type");
                if (type.equalsIgnoreCase("equity")) {
                    instrument = new Equity(rs.getString("ticker"), rs.getString("name"), rs.getString("sector"));
                } else if (type.equalsIgnoreCase("etf")) {
                    List<Constituent> constituents = new ArrayList<>();
                    constituents.add(new Constituent(rs.getString("constituent_ticker"), rs.getDouble("weight")));
                    instrument = new Etf(rs.getString("ticker"), rs.getString("name"), rs.getString("description"), rs.getString("inception_date"), null);
                } else {
                    throw new StatusCodeException(StatusCode.NOT_FOUND, "Error: Could not find type " + type);
                }

                instruments.add(instrument);
            }
        }

        return instruments;
    }
}
