package uk.co.asepstrath.bank.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Constituent;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.repository.EquityRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EquityRepositoryTest {

    private DataSource ds;
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private EquityRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
        ds = mock(DataSource.class);
        conn = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        repo = new EquityRepository(ds);
    }

    @Test
    void saveAll() throws SQLException {
        repo.saveAll(List.of(new Equity("Apple", "AAPL", "Technology")), List.of(new Etf("Apples", "AAPLS", "Apple but plural", "20-03-2026", List.of(new Constituent("AAPL", 0.5), new Constituent("BBPL", 0.5)))));
        verify(ps, times(1)).setString(1, "AAPL");
        verify(ps, times(1)).setString(2, "Apple");
        verify(ps, times(1)).setString(3, "Technology");
        verify(ps, times(1)).setString(1, "AAPLS");
        verify(ps, times(1)).setString(2, "Apples");
        verify(ps, times(1)).setString(3, null);

        verify(ps, times(2)).addBatch();
    }

    @Test
    void saveAll_connectionFails() throws SQLException {
        when(ds.getConnection()).thenThrow(new SQLException("Connection Failed"));
        assertThrows(SQLException.class, () -> repo.saveAll(List.of(new Equity("Apple", "AAPL", "Technology")), List.of(new Etf("Apples", "AAPLS", "Apple but plural", "20-03-2026", List.of(new Constituent("AAPL", 0.5), new Constituent("BBPL", 0.5))))));
    }

    @Test
    void saveAll_equitiesNull() throws SQLException {
        repo.saveAll(null, List.of(new Etf("Apples", "AAPLS", "Apple but plural", "20-03-2026", List.of(new Constituent("AAPL", 0.5), new Constituent("BBPL", 0.5)))));

        verify(ps, never()).setString(1, "AAPL");
        verify(ps, never()).setString(1, "BBPL");


        verify(ps, times(1)).setString(1, "AAPLS");
        verify(ps, times(1)).setString(2, "Apples");
        verify(ps, times(1)).setString(3, null);

        verify(ps, times(1)).addBatch();
    }

    @Test
    void saveAll_etfsNull() throws SQLException {
        repo.saveAll(List.of(new Equity("Apple", "AAPL", "Technology")), null);

        verify(ps, never()).setString(3, null);


        verify(ps, times(1)).setString(1, "AAPL");
        verify(ps, times(1)).setString(2, "Apple");
        verify(ps, times(1)).setString(3, "Technology");

        verify(ps, times(1)).addBatch();
    }

    @Test
    void getAll() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("ticker")).thenReturn("AAPL");
        when(rs.getString("name")).thenReturn("Apple");
        when(rs.getString("sector")).thenReturn("Technology");

        List<Equity> equities = repo.getAll();

        assertFalse(equities.isEmpty());
        assertEquals(1, equities.size());
        assertEquals("AAPL", equities.getFirst().getTicker());
        assertEquals("Apple", equities.getFirst().getName());
        assertEquals("Technology", equities.getFirst().getSector());
    }

    @Test
    void getAll_noEquities() throws SQLException {
        when(rs.next()).thenReturn(false);
        List<Equity> equities = repo.getAll();
        assertTrue(equities.isEmpty());
    }

    @Test
    void getEquity() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("ticker")).thenReturn("AAPL");
        when(rs.getString("name")).thenReturn("Apple");
        when(rs.getString("sector")).thenReturn("Technology");

        Equity equity = repo.getEquity("AAPL");
        assertEquals("AAPL", equity.getTicker());
        assertEquals("Apple", equity.getName());
        assertEquals("Technology", equity.getSector());
    }

    @Test
    void getEquity_noEquities() throws SQLException {
        when(rs.next()).thenReturn(false);
        Equity equity = repo.getEquity("x");
        assertNull(equity);
    }

    @Test
    void getEquity_noEquitiesForTicker() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        Equity equity = repo.getEquity("x");
        assertNotNull(equity);
        assertNull(equity.getTicker());
        assertNull(equity.getName());
        assertNull(equity.getSector());
    }
}
