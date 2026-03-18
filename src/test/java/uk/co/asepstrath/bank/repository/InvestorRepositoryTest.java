package uk.co.asepstrath.bank.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InvestorRepositoryTest {

    private DataSource ds;
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private InvestorRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        ds = mock(DataSource.class);
        conn = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        repository = new InvestorRepository(ds);
    }

    @Test
    void saveAllHappyPath() throws SQLException {
        repository.saveAll(List.of(new Investor("1", "John Doe", 100.0)));
        verify(ps).setString(1, "1");
        verify(ps).setString(2, "John Doe");
        verify(ps).setDouble(3, 100.0);
    }

    @Test
    void saveAllConnectionFails() throws SQLException {
        when(ds.getConnection()).thenThrow(new SQLException("Connection Failed"));
        assertThrows(SQLException.class, () -> repository.saveAll(List.of (new Investor("1", "John Doe", 100))));
    }

    @Test
    void saveAllRoundsCorrect () throws SQLException {
        repository.saveAll(List.of(new Investor("1", "John Doe", 99.999)));
        verify(ps).setDouble(3, 99.99);
    }


    @Test
    void getAllHappyPath() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("id")).thenReturn("1");
        when(rs.getString("name")).thenReturn("John Doe");
        when(rs.getDouble("cash_balance")).thenReturn(100.0);
        InvestorDTO result = repository.getAll();

        assertEquals(1, result.getInvestors().size());
        assertEquals("1", result.getInvestors().getFirst().getId());
        assertEquals("John Doe", result.getInvestors().getFirst().getName());
        assertEquals(100.0, result.getInvestors().getFirst().getCashBalance());
    }

    @Test
    void getAllWhenNoInvestor () throws SQLException {
        when(rs.next()).thenReturn(false);
        InvestorDTO investorDTO = repository.getAll();
        assertTrue(investorDTO.getInvestors().isEmpty());
    }

    @Test
    void saveAllMultipleInvestors () throws SQLException {
        repository.saveAll(List.of (new Investor("1", "John Doe", 100.0), new Investor("2", "Jane Doe", 99.999)));
        verify(ps, times(2)).addBatch();
    }

    @Test
    void getInvestor() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("id")).thenReturn("1");
        when(rs.getString("name")).thenReturn("John Doe");
        when(rs.getDouble("cash_balance")).thenReturn(100.0);
        Investor investor = repository.getInvestor("1");

        assertNotNull(investor);
        assertEquals("1", investor.getId());
        assertEquals("John Doe", investor.getName());
        assertEquals(100.0, investor.getCashBalance());
    }

    @Test
    void getInvestorWhenNoInvestors () throws SQLException {
        when(rs.next()).thenReturn(false);
        Investor investor = repository.getInvestor("1");
        assertNull(investor);
    }
}