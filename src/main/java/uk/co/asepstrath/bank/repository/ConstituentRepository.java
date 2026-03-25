package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Constituent;
import uk.co.asepstrath.bank.model.Etf;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ConstituentRepository {

    private final DataSource ds;


    public ConstituentRepository(DataSource ds) {
        this.ds = ds;
    }

    public void setUp() throws SQLException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS constituents (
                            equity_ticker VARCHAR(20) REFERENCES equity(ticker),
                            etf_ticker VARCHAR(20) REFERENCES etf(ticker),
                            weight DOUBLE,
                            PRIMARY KEY (equity_ticker, etf_ticker)
                        )
                    """);
        }
    }

    public void saveAll(List<Etf> etfs) throws SQLException {
        String sql = """
                MERGE INTO constituents (equity_ticker, etf_ticker, weight)
                KEY (equity_ticker, etf_ticker)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            for (Etf etf : etfs) {
                String etfTicker = etf.getTicker();
                ps.setString(2, etfTicker);
                List<Constituent> constituents = etf.getConstituents();

                for (Constituent constituent : constituents) {
                    ps.setString(1, constituent.getTicker());
                    ps.setDouble(3, constituent.getWeight());
                    ps.addBatch();
                }
            }

            ps.executeBatch();
            conn.commit();
        }
    }
}
