package uk.co.asepstrath.bank.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class TransactionRepositoryTests {

    private DataSource ds;
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private TransactionRepository repository;

    @BeforeEach
    void setUp () throws SQLException {
        ds = mock(DataSource.class);
        conn = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        repository = new TransactionRepository(ds);
    }

    @Test
    void saveAll () throws SQLException {
        repository.saveAll(List.of(new Transaction("1", "12.20", "DEPOSIT", "1", "AWSM", 100.0)));
        verify(ps).setString(1, "1");
        verify(ps).setString(2, "12.20");
        verify(ps).setString(3, "DEPOSIT");
        verify(ps).setString(4, "1");
        verify(ps).setString(5, "AWSM");
        verify(ps).setDouble(6, 100.0);
        verify(ps, times(1)).addBatch();
        verify(ps).executeBatch();
    }

    @Test
    void saveAllConnectionFails() throws SQLException {
        when(ds.getConnection()).thenThrow(new SQLException("Connection Failed"));
        assertThrows(SQLException.class, () -> repository.saveAll(List.of(new Transaction("1", "12.20", "DEPOSIT", "1", "AWSM", 100.0))));
    }

    @Test
    void saveAllMultipleInvestors () throws SQLException {
        repository.saveAll(List.of(new Transaction("1", "12.20", "DEPOSIT", "1", "AWSM", 100.0), new Transaction("2", "12.20", "DEPOSIT", "2", "AWSM", 100.0)));
        verify(ps, times(2)).addBatch();
    }

    @Test
    void getAll () throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("id")).thenReturn("1");
        when(rs.getString("timestamp")).thenReturn("12.20");
        when(rs.getString("type")).thenReturn("DEPOSIT");
        when(rs.getString("investor_id")).thenReturn("1");
        when(rs.getString("ticker")).thenReturn("AWSM");
        when(rs.getDouble("total_cash_amount")).thenReturn(100.0);
        TransactionDTO result = repository.getAll();

        assertEquals(1, result.getTransactions().size());
        assertEquals("1", result.getTransactions().getFirst().getId());
        assertEquals("12.20", result.getTransactions().getFirst().getTimestamp());
        assertEquals("DEPOSIT", result.getTransactions().getFirst().getType());
        assertEquals("1", result.getTransactions().getFirst().getInvestorId());
        assertEquals("AWSM", result.getTransactions().getFirst().getTicker());
        assertEquals(100.0, result.getTransactions().getFirst().getTotalCashAmount());
    }

    @Test
    void getAllWhenNoTransaction () throws SQLException {
        when(rs.next()).thenReturn(false);
        TransactionDTO transactionDTO = repository.getAll();
        assertTrue(transactionDTO.getTransactions().isEmpty());
    }

    @Test
    void getTransaction () throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("id")).thenReturn("1");
        when(rs.getString("timestamp")).thenReturn("12.20");
        when(rs.getString("type")).thenReturn("DEPOSIT");
        when(rs.getString("investor_id")).thenReturn("1");
        when(rs.getString("ticker")).thenReturn("AWSM");
        when(rs.getDouble("total_cash_amount")).thenReturn(100.0);
        List<Transaction> result = repository.getTransactionsByInvestor("1");

        assertEquals(1, result.size());
        assertEquals("1", result.getFirst().getId());
        assertEquals("12.20", result.getFirst().getTimestamp());
        assertEquals("DEPOSIT", result.getFirst().getType());
        assertEquals("1", result.getFirst().getInvestorId());
        assertEquals("AWSM", result.getFirst().getTicker());
        assertEquals(100.0, result.getFirst().getTotalCashAmount());
    }
}
