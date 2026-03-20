package uk.co.asepstrath.bank.unit.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.repository.PriceRepository;
import uk.co.asepstrath.bank.service.PriceService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceServiceTest {

    private PriceRepository repo;
    private PriceService service;

    @BeforeEach
    void setUp () {
        repo = mock(PriceRepository.class);
        service = new PriceService(repo);
    }

    @Test
    void getLastPrice () throws SQLException {
        when(repo.getLatestPrice("AAPL")).thenReturn(100.0);
        double latest = service.getLatestPrice("AAPL");
        assertEquals(100.0, latest);
    }

    @Test
    void getLastPriceNoShare_throws404 () throws SQLException {
        when(repo.getLatestPrice(anyString())).thenReturn(Double.MAX_VALUE);
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getLatestPrice("AAPL"));
        assertEquals(StatusCode.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getLastPrice_throws500 () throws SQLException {
        when(repo.getLatestPrice(anyString())).thenThrow(SQLException.class);
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getLatestPrice("AAPLE"));
        assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
    }

    @Test
    void getPriceAtTimestamp () throws SQLException {
        when(repo.getPriceAtTime("10.00", "AAPL")).thenReturn(100.0);
        double priceAtTime = service.getPriceAtTimestamp("10.00", "AAPL");
        assertEquals(100.0, priceAtTime);
    }


    @Test
    void getPriceAtTimeNoPrice_throws404 () throws SQLException {
        when(repo.getPriceAtTime(anyString(), anyString())).thenReturn(Double.MAX_VALUE);
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getPriceAtTimestamp("9.40", "AAPL"));
        assertEquals(StatusCode.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getPriceAtTimeNoPrice_throws500 () throws SQLException {
        when(repo.getPriceAtTime(anyString(), anyString())).thenThrow(SQLException.class);
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getPriceAtTimestamp("9.40", "AAPL"));
        assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
    }
}
