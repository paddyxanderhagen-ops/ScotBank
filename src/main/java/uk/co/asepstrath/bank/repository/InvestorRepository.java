package uk.co.asepstrath.bank.repository;
import uk.co.asepstrath.bank.model.Investor;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


public class InvestorRepository {

    private final DataSource ds;

    public InvestorRepository(DataSource ds) {
        this.ds = ds;
    }

    public void saveAll(List<Investor> investors) throws Exception {
        try (var conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            try (var stmt = conn.prepareStatement("""
                INSERT INTO investor (id, name, cash_balance)
                VALUES (?, ?, ?)
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
}
