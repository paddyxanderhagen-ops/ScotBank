package uk.co.asepstrath.bank.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.repository.PriceRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PriceRepositoryTest {
    private DataSource ds;
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private PriceRepository repository;

    @BeforeEach
    void setUp() throws SQLException {
        ds = mock(DataSource.class);
        conn = mock(Connection.class);
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        repository = new PriceRepository(ds);
    }

    @Test
    void saveAll() throws SQLException {
        repository.saveAll("AAPL", List.of(new Price("10.00", 150.0), new Price("10.15", 100.0)));
        verify(ps, times(2)).setString(1, "AAPL");
        verify(ps, times(1)).setString(2, "10.00");
        verify(ps, times(1)).setDouble(3, 150.0);
        verify(ps, times(1)).setString(2, "10.15");
        verify(ps, times(1)).setDouble(3, 100.0);
        verify(ps, times(2)).addBatch();
    }

    @Test
    void getPricesForStock() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getDouble("price")).thenReturn(150.0);
        List<Double> prices = repository.getPricesForStock("AAPL");
        verify(ps).setString(1, "AAPL");
        assertEquals(150.0, prices.getFirst());
    }

    @Test
    void getPriceAtTime() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getDouble("price")).thenReturn(150.0);
        double price = repository.getPriceAtTime("10.00", "AAPL");
        verify(ps).setString(1, "10.00");
        verify(ps).setString(2, "AAPL");
        assertEquals(150.0, price);
    }

    @Test
    void getLatestPrice() throws SQLException {
        when(rs.next()).thenReturn(true, false);
        when(rs.getDouble("price")).thenReturn(150.0);
        double price = repository.getLatestPrice("AAPL");
        verify(ps).setString(1, "AAPL");
        assertEquals(150.0, price);
    }
}
