package uk.co.asepstrath.bank.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InvestorRepositoryTest {

    private DataSource ds = mock(DataSource.class);
    private Connection conn = mock(Connection.class);
    private PreparedStatement ps = mock(PreparedStatement.class);
    private ResultSet rs = mock(ResultSet.class);
    private InvestorRepository repository = mock(InvestorRepository.class);

    @BeforeEach
    void setUp() throws SQLException {
        when(ds.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        repository = new InvestorRepository(ds);

        repository.saveAll(List.of(new Investor("1", "John Doe", 100.0)));
    }

    @Test
    void saveAllHappyPath() throws SQLException {
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
        verify(rs).getString("id");
        verify(rs).getString("name");
        verify(rs).getDouble("cash_balance");

        assertEquals(1, result.getInvestors().size());
        assertEquals("1", result.getInvestors().getFirst().getId());
        assertEquals("John Doe", result.getInvestors().getFirst().getName());
        assertEquals(100.0, result.getInvestors().getFirst().getCashBalance());
    }

    @Test
    void getInvestor() throws SQLException {

    }
}