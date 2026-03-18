package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class InvestorRepository {

    private final DataSource ds;

    public InvestorRepository(DataSource ds) {
        this.ds = ds;
    }

    public void saveAll(List<Investor> investors) throws SQLException {
        try (var conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            try (var stmt = conn.prepareStatement("""
                MERGE INTO investor (id, name, cash_balance)
                VALUES (?, ?, ?);
            """)) {

                for (Investor i : investors) {
                    stmt.setString(1, i.getId());
                    stmt.setString(2, i.getName());

                    //round the balance to 2 decimal places
                    double newBalance = i.getCashBalance();
                    double roundedUp = BigDecimal.valueOf(newBalance).setScale(2, RoundingMode.FLOOR).doubleValue();


                    stmt.setDouble(3, roundedUp);
                    stmt.addBatch();
                }

                stmt.executeBatch();
                conn.commit();
            }
        }
    }

    public InvestorDTO getAll () throws SQLException {
        InvestorDTO investorDTO = new InvestorDTO();
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            String sql = """
                    SELECT *
                    FROM investor;""";



            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    Investor investor = new Investor(resultSet.getString("id"), resultSet.getString("name"), resultSet.getDouble("cash_balance"));
                    investorDTO.addInvestor(investor);
                }
            }
        }
        return investorDTO;
    }

    public Investor getInvestor (String id) throws SQLException {
        Investor investor = null;

        try (Connection conn = ds.getConnection()) {
            String sql = """
                    SELECT id, name, cash_balance
                    FROM investor
                    WHERE investor.id = ?""";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                ResultSet resultSet = stmt.executeQuery();

                if (resultSet.next()) {
                    investor = new Investor(resultSet.getString("id"), resultSet.getString("name"), resultSet.getDouble("cash_balance"));
                }
            }
        }

        return investor;
    }
}
