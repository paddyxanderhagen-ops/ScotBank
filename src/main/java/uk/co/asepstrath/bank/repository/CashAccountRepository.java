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

    public void createAccount(long id, String ownerName, BigDecimal balance) throws SQLException {
        String sql = "INSERT INTO cash_account (id, owner_name, balance) VALUES (?, ?, ?)";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, ownerName);
            ps.setBigDecimal(3, balance);
            ps.executeUpdate();
        }
    }

    public double getBalance (String id) throws SQLException {
        String sql = "SELECT balance FROM cash_account WHERE id = ?";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

            throw new IllegalArgumentException("Account not found");

        }
    }

    public void transaction(String investorId, double updatedBalance) throws SQLException{
        String sql = """
                UPDATE investor
                SET cash_balance = ?
                WHERE id = ?;""";

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, updatedBalance);
            ps.setString(2, investorId);

            ps.execute();
        }
    }
}