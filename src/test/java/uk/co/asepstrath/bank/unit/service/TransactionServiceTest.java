package uk.co.asepstrath.bank.unit.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;
import uk.co.asepstrath.bank.service.TransactionService;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {
    private TransactionRepository repository;
    private TransactionService service;

    @BeforeEach
    void setUp () {
        repository = mock(TransactionRepository.class);
        service = new TransactionService(repository);
    }

    @Test
    void getInvestor () throws SQLException {
        when(repository.getTransactionsByInvestor(anyString())).thenReturn(List.of (new Transaction("1", "12.20", "DEPOSIT", "1", "AWSM", 100.0)));
        List<Transaction> t = service.getTransactionsByInvestor("1");
        verify(repository).getTransactionsByInvestor("1");

        assertEquals(1, t.size());
        assertEquals("1", t.getFirst().getId());
        assertEquals("12.20", t.getFirst().getTimestamp());
        assertEquals("DEPOSIT", t.getFirst().getType());
        assertEquals("1", t.getFirst().getInvestorId());
        assertEquals("AWSM", t.getFirst().getTicker());
        assertEquals(100.0, t.getFirst().getTotalCashAmount());
    }

    @Test
    void getInvestor_throws404 () throws SQLException {
        when(repository.getTransactionsByInvestor(anyString())).thenReturn(null);
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getTransactionsByInvestor("x"));
        assertEquals(StatusCode.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getInvestor_throws500 () throws SQLException {
        when(repository.getTransactionsByInvestor(anyString())).thenThrow(new SQLException());
        StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getTransactionsByInvestor("x"));
        assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
    }
}
