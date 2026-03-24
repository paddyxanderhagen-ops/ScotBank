package uk.co.asepstrath.bank.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Constituent;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.Instrument;
import uk.co.asepstrath.bank.repository.InstrumentRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstrumentRepositoryTest {

    private DataSource ds;
    private Connection conn;
    private PreparedStatement ps;
    private Statement stmt;
    private ResultSet rs;
    private InstrumentRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
        ds = mock(DataSource.class);
        conn = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        stmt = mock(Statement.class);
        rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.createStatement()).thenReturn(stmt);
        when(conn.prepareStatement(anyString())).thenReturn(ps);

        repo = new InstrumentRepository(ds);
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
    void saveAll_insertsEquitiesAndEtfs() throws SQLException {
        Equity equity = new Equity("AAPL", "Apple Inc.", "Technology");
        Etf etf = new Etf("TECH", "Tech ETF", "Tech sector ETF", "2020-01-01", List.of(
                new Constituent("AAPL", 0.5)
        ));

        repo.saveAll(List.of(equity), List.of(etf));

        verify(ps, times(2)).addBatch();
        verify(ps).executeBatch();
        verify(ps).close();
        verify(conn).close();
    }

    @Test
    void getInstrument_returnsEquity() throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getString("type")).thenReturn("equity");
        when(rs.getString("ticker")).thenReturn("AAPL");
        when(rs.getString("name")).thenReturn("Apple Inc.");
        when(rs.getString("sector")).thenReturn("Technology");

        Instrument instrument = repo.getInstrument("AAPL");

        assertNotNull(instrument);
        assertInstanceOf(Equity.class, instrument);
        assertEquals("AAPL", instrument.getTicker());
        assertEquals("Apple Inc.", instrument.getName());
        verify(ps).close();
        verify(conn).close();
    }

    @Test
    void getInstrument_returnsEtfWithConstituents() throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("type")).thenReturn("etf", "etf");
        when(rs.getString("ticker")).thenReturn("TECH", "TECH");
        when(rs.getString("name")).thenReturn("Tech ETF", "Tech ETF");
        when(rs.getString("description")).thenReturn("Tech sector ETF", "Tech sector ETF");
        when(rs.getString("inception_date")).thenReturn("2020-01-01", "2020-01-01");
        when(rs.getString("constituent_ticker")).thenReturn("AAPL", "GOOG");
        when(rs.getDouble("weight")).thenReturn(0.5, 0.3);

        Instrument instrument = repo.getInstrument("TECH");

        assertNotNull(instrument);
        assertInstanceOf(Etf.class, instrument);
        Etf etf = (Etf) instrument;
        assertEquals("TECH", etf.getTicker());
        assertEquals(2, etf.getConstituents().size());
        verify(ps).close();
        verify(conn).close();
    }

    @Test
    void getAll_returnsListOfInstruments() throws SQLException {
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, true, false);
        when(rs.getString("type")).thenReturn("equity", "etf");
        when(rs.getString("ticker")).thenReturn("AAPL", "TECH");
        when(rs.getString("name")).thenReturn("Apple Inc.", "Tech ETF");
        when(rs.getString("sector")).thenReturn("Technology", null);
        when(rs.getString("description")).thenReturn(null, "Tech sector ETF");
        when(rs.getString("inception_date")).thenReturn(null, "2020-01-01");
        when(rs.getString("constituent_ticker")).thenReturn(null, "AAPL");
        when(rs.getDouble("weight")).thenReturn(0.0, 0.5);

        List<Instrument> instruments = repo.getAll();

        assertEquals(2, instruments.size());
        assertInstanceOf(Equity.class, instruments.get(0));
        assertInstanceOf(Etf.class, instruments.get(1));
        verify(ps).close();
        verify(conn).close();
    }
}