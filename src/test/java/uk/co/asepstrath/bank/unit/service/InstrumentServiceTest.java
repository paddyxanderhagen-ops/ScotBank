package uk.co.asepstrath.bank.unit.service;

import io.jooby.exception.StatusCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Instrument;
import uk.co.asepstrath.bank.repository.InstrumentRepository;
import uk.co.asepstrath.bank.service.InstrumentService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstrumentServiceTest {

    private InstrumentRepository repo;
    private InstrumentService service;

    @BeforeEach
    void setUp() {
        repo = mock(InstrumentRepository.class);
        service = new InstrumentService(repo);
    }

    @Test
    void getInstrument_returnsInstrument_whenFound() throws SQLException {
        Instrument mockInstrument = new Equity("AAPL", "Apple Inc.", "Public");
        when(repo.getInstrument("AAPL")).thenReturn(mockInstrument);

        Instrument result = service.getInstrument("AAPL");

        assertNotNull(result);
        assertEquals("AAPL", result.getTicker());
        verify(repo, times(1)).getInstrument("AAPL");
    }

    @Test
    void getInstrument_returnsNull_whenNotFound() throws SQLException {
        when(repo.getInstrument("AAPL")).thenReturn(null);

        Instrument result = service.getInstrument("AAPL");

        assertNull(result);
        verify(repo).getInstrument("AAPL");
    }

    @Test
    void getInstrument_throwsException_onSQLException() throws SQLException {
        when(repo.getInstrument("AAPL")).thenThrow(new SQLException("DB error"));

        StatusCodeException exception = assertThrows(
                StatusCodeException.class,
                () -> service.getInstrument("AAPL")
        );

        assertTrue(exception.getMessage().contains("Failed to retrieve instrument"));
    }

    @Test
    void getAll_returnsList_whenNotEmpty() throws SQLException {
        List<Instrument> mockList = List.of(
                new Equity("AAPL", "Apple Inc.", "Public"),
                new Equity("GOOG", "Google", "Private")
        );

        when(repo.getAll()).thenReturn(mockList);

        List<Instrument> result = service.getAll();

        assertEquals(2, result.size());
        verify(repo).getAll();
    }

    @Test
    void getAll_throwsNotFound_whenEmptyList() throws SQLException {
        when(repo.getAll()).thenReturn(Collections.emptyList());

        StatusCodeException exception = assertThrows(
                StatusCodeException.class,
                () -> service.getAll()
        );

        assertEquals("Instruments not found", exception.getMessage());
    }

    @Test
    void getAll_throwsServerError_onSQLException() throws SQLException {
        when(repo.getAll()).thenThrow(new SQLException());

        StatusCodeException exception = assertThrows(
                StatusCodeException.class,
                () -> service.getAll()
        );

        assertEquals("Failed to retrieve instruments", exception.getMessage());
    }
}