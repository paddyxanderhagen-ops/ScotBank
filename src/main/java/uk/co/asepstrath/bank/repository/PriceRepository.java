package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Price;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceRepository {

    private final DataSource ds;

    public PriceRepository(DataSource ds) {
        this.ds = ds;
    }

    public void setUp() throws SQLException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS price (
                        ticker VARCHAR(20),
                        timestamp VARCHAR(30),
                        price DOUBLE,
                        PRIMARY KEY (ticker, timestamp),
                        FOREIGN KEY (ticker) REFERENCES equity(ticker)
                    )
                    """);
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_price_ticker ON price(ticker)");
        }
    }

    public void saveAll(String ticker, List<Price> prices) throws SQLException {
        String sql = """
                MERGE INTO price (ticker, timestamp, price)
                KEY(ticker, timestamp)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (Price price : prices) {
                ps.setString(1, ticker);
                ps.setString(2, price.getTimeStamp());
                ps.setDouble(3, price.getPrice());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        }
    }

    public List<Double> getPricesForStock(String ticker) throws SQLException {
        String sql = """
                SELECT price FROM price WHERE ticker = ?
                """;
        List<Double> prices = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticker);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prices.add(rs.getDouble("price"));
            }

            return prices;
        }
    }

    public double getPriceAtTime(String timestamp, String ticker) throws SQLException {
        String sql = """
                SELECT price FROM price WHERE timestamp = ? AND ticker = ?
                """;
        double price = Double.MAX_VALUE;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, timestamp);
            ps.setString(2, ticker);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                price = rs.getDouble("price");
            }

            return price;
        }
    }

    public double getLatestPrice(String ticker) throws SQLException {
        String sql = """
                SELECT price FROM price WHERE ticker = ? ORDER BY timestamp DESC LIMIT 1
                """;

        double price = Double.MAX_VALUE;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticker);

            ResultSet rs = ps.executeQuery();
            rs.next();

            price = rs.getDouble("price");

            return price;
        }
    }
}
