package uk.co.asepstrath.bank.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Constituent;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.repository.ConstituentRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class ConstituentRepositoryTest {

    private DataSource ds;
    private Connection conn;
    private Statement stmt;
    private PreparedStatement ps;
    private ConstituentRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
        ds = mock(DataSource.class);
        conn = mock(Connection.class);
        stmt = mock(Statement.class);
        ps = mock(PreparedStatement.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.createStatement()).thenReturn(stmt);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        repo = new ConstituentRepository(ds);
    }

    @Test
    void setUp_executesCreateTable() throws SQLException {
        repo.setUp();
        verify(conn).createStatement();
        verify(stmt).execute(anyString());
        verify(stmt).close();
        verify(conn).close();
    }

    @Test
    void saveAll_executesBatchInsert() throws SQLException {
        Constituent c1 = new Constituent("AAPL", 0.5);
        Constituent c2 = new Constituent("GOOG", 0.3);

        Etf etf = new Etf(
                "TECH",
                "Tech ETF",
                "Technology sector ETF",
                "2020-01-01",
                List.of(c1, c2)
        );

        assertDoesNotThrow(() -> repo.saveAll(List.of(etf)));

        verify(conn).prepareStatement(anyString());
        verify(ps, times(2)).addBatch();
        verify(ps).executeBatch();
        verify(conn).commit();
        verify(conn).setAutoCommit(false);
        verify(ps).close();
        verify(conn).close();
    }

    @Test
    void saveAll_rollsBackOnException() throws SQLException {
        when(conn.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        Constituent c = new Constituent("AAPL", 0.5);
        Etf etf = new Etf(
                "TECH",
                "Tech ETF",
                "Technology sector ETF",
                "2020-01-01",
                List.of(c)
        );

        try {
            repo.saveAll(List.of(etf));
        } catch (SQLException e) {
            // expected
        }

        verify(conn).close();
    }
}