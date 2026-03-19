package uk.co.asepstrath.bank.unit.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.service.InvestorService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InvestorServiceTest {
    private InvestorRepository repository;
    private InvestorService service;

    @BeforeEach
    void setUp () {
        repository = mock(InvestorRepository.class);
        service = new InvestorService(repository);
    }

    @Test
    void getInvestor () throws SQLException {
        when(repository.getInvestor(anyString())).thenReturn(new Investor("1", "John Doe", 100.0));
        Investor i = service.getInvestor("1");
        verify(repository).getInvestor("1");
        assertEquals("1", i.getId());
        assertEquals("John Doe", i.getName());
        assertEquals(100.0, i.getCashBalance());
    }

    @Test
    void getInvestor_throws404 () throws SQLException {
        when(repository.getInvestor(anyString())).thenReturn(null);
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getInvestor("x"));
        assertEquals(StatusCode.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getInvestor_throws500 () throws SQLException {
        when(repository.getInvestor(anyString())).thenThrow(new SQLException());
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getInvestor("x"));
        assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
    }
}