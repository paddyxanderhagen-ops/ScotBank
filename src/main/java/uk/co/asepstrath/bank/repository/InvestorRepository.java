package uk.co.asepstrath.bank.repository;
import uk.co.asepstrath.bank.model.Investor;
import javax.sql.DataSource;
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
                INSERT INTO investor (id, name)
                VALUES (?, ?)
            """)) {

                for (Investor i : investors) {
                    stmt.setString(1, i.getId());
                    stmt.setString(2, i.getName());
                    stmt.addBatch();
                }

                stmt.executeBatch();
                conn.commit();
            }
        }
    }
}
