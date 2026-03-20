package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Price;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquitiesRepository {

    private DataSource ds;

    public EquitiesRepository(DataSource ds) {
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
}
