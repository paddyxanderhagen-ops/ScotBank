package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Equity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquityRepository {

    private DataSource ds;

    public EquityRepository(DataSource ds) {
        this.ds = ds;
    }

    public void setUp() throws SQLException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS equity (
                            ticker VARCHAR(20) PRIMARY KEY,
                            name VARCHAR(100),
                            sector VARCHAR (100)
                        )
                    """);
        }
    }

    public void saveAll(Equity equity) throws SQLException {
        String sql = """
                MERGE INTO equity (ticker, name, sector)
                VALUES (?, ?, ?)
                """;


        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            List<Double> pricePerTicker = new ArrayList<>();
            List<String> timestampPerTicker = new ArrayList<>();


            ps.setString(1, equity.getTicker());
            ps.setString(2, equity.getName());
            ps.setString(3, equity.getSector());
            ps.executeUpdate();
        }
    }

    public List<Equity> getAll() throws SQLException {
        String sql = """
                SELECT * FROM equity
                """;

        List<Equity> equities = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equities.add(new Equity(rs.getString("ticker"), rs.getString("name"), rs.getString("sector")));
            }

            return equities;
        }
    }

    public Equity getEquity(String ticker) throws SQLException {
        String sql = """
                SELECT * FROM equity
                WHERE ticker = ?""";

        Equity equity = null;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ticker);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                equity = new Equity(rs.getString("ticker"), rs.getString("name"), rs.getString("sector"));
            }

            return equity;
        }
    }
}
