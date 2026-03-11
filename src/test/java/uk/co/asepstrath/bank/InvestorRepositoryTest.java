package uk.co.asepstrath.bank;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvestorRepositoryTest {

    private InvestorRepository repository;
    private DataSource ds;

    @BeforeEach
    void setup() throws Exception {
        JdbcDataSource jdbc = new JdbcDataSource();
        jdbc.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        ds = jdbc;
        repository = new InvestorRepository(ds);

        try (var conn = ds.getConnection();
             var stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS investor");

            stmt.execute("""
                CREATE TABLE investor (
                    id VARCHAR(36) PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    cash_balance DECIMAL(15,2) NOT NULL
                )
            """);
        }
    }

    @Test
    void saveAll_shouldInsertSingleInvestor() throws Exception {
        List<Investor> investors = List.of(
                new Investor("1", "Alice", 100.25)
        );

        repository.saveAll(investors);

        try (var conn = ds.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM investor")) {

            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void saveAll_shouldInsertMultipleInvestors() throws Exception {
        List<Investor> investors = List.of(
                new Investor("1", "Alice", 10.50),
                new Investor("2", "Bob", 20.75)
        );

        repository.saveAll(investors);

        try (var conn = ds.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM investor")) {

            rs.next();
            assertEquals(2, rs.getInt(1));
        }
    }

    @Test
    void saveAll_shouldRoundDownToTwoDecimals() throws Exception {
        List<Investor> investors = List.of(
                new Investor("1", "Alice", 10.999)
        );

        repository.saveAll(investors);

        try (var conn = ds.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT cash_balance FROM investor WHERE id='1'")) {

            rs.next();
            assertEquals(10.99, rs.getDouble(1));
        }
    }

    @Test
    void saveAll_withEmptyList_shouldInsertNothing() throws Exception {
        repository.saveAll(List.of());

        try (var conn = ds.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM investor")) {

            rs.next();
            assertEquals(0, rs.getInt(1));
        }
    }

    @Test
    void shouldPersistAndReadBackInvestor() throws Exception {
        var investors = List.of(
                new Investor("abc", "Bob", 12.34)
        );

        repository.saveAll(investors);

        try (var conn = ds.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT name, cash_balance FROM investor WHERE id = 'abc'")) {

            rs.next();

            assertEquals("Bob", rs.getString("name"));
            assertEquals(12.34, rs.getDouble("cash_balance"));
        }
    }
}