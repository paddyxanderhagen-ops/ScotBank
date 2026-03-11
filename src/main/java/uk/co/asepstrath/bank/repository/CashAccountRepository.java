package uk.co.asepstrath.bank.repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CashAccountRepository {

    private final DataSource ds;

    public CashAccountRepository(DataSource ds) {
        this.ds = ds;
    }

    public void createAccount(long id, String ownerName, BigDecimal balance) {
        String sql = "INSERT INTO cash_account (id, owner_name, balance) VALUES (?, ?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, ownerName);
            ps.setBigDecimal(3, balance);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getBalance(long id) {
        String sql = "SELECT balance FROM cash_account WHERE id = ?";

        try (var conn = ds.getConnection();
             var ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

            throw new IllegalArgumentException("Account not found");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void transaction (String investorId, double updatedBalance) {
        try (Connection conn = ds.getConnection()) {
            String sql = """
                    UPDATE investor
                    SET cash_balance = ?
                    WHERE id = ?;""";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, updatedBalance);
                stmt.setString(2, investorId);

                stmt.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}