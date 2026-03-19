package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private final DataSource ds;

    public TransactionRepository(DataSource ds) {
        this.ds = ds;
    }

    public void saveAll(List<Transaction> transactions) throws SQLException {
        try (var conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            try (var stmt = conn.prepareStatement("""
                MERGE INTO transaction 
                (id, timestamp, type, investor_id, ticker, total_cash_amount)
                VALUES (?, ?, ?, ?, ?, ?)
            """)) {

                for (Transaction t : transactions) {

                    stmt.setString(1, t.getId());
                    stmt.setString(2, t.getTimestamp());
                    stmt.setString(3, t.getType());
                    stmt.setString(4, t.getInvestorId());
                    stmt.setString(5, t.getTicker());
                    stmt.setDouble(6, t.getTotalCashAmount());

                    stmt.addBatch();
                }

                stmt.executeBatch();
                conn.commit();
            }
        }
    }

    public TransactionDTO getAll () throws SQLException{
        TransactionDTO transactionDTO = new TransactionDTO();
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            String sql = """
                    SELECT *
                    FROM transaction;""";


            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    Transaction transaction = new Transaction(resultSet.getString("id"), resultSet.getString("timestamp"), resultSet.getString("type"), resultSet.getString("investor_id"), resultSet.getString("ticker"), resultSet.getDouble("total_cash_amount"));
                    transactionDTO.addTransaction(transaction);
                }
            }
        }
        return transactionDTO;
    }

    public List<Transaction> getTransactionsByInvestor (String id) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = ds.getConnection()) {
            String sql = """
                    SELECT *
                    FROM transaction
                    WHERE investor_id = ?""";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                ResultSet resultSet = stmt.executeQuery();
                Transaction transaction = null;

                while (resultSet.next()) {
                    transaction = new Transaction(resultSet.getString("id"), resultSet.getString("timestamp"), resultSet.getString("type"), resultSet.getString("investor_id"), resultSet.getString("ticker"), resultSet.getDouble("total_cash_amount"));
                }
                transactions.add(transaction);
            }
        }

        return transactions;
    }
}