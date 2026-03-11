package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Transaction;

import javax.sql.DataSource;
import java.util.List;

public class TransactionRepository {

    private final DataSource ds;

    public TransactionRepository(DataSource ds) {
        this.ds = ds;
    }

    public void saveAll(List<Transaction> transactions) throws Exception {

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
                    stmt.setBigDecimal(6, t.getTotalCashAmount());

                    stmt.addBatch();
                }

                stmt.executeBatch();
                conn.commit();
            }
        }
    }
}