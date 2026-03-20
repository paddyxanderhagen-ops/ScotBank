package uk.co.asepstrath.bank.unit.service;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.PriceRepository;
import uk.co.asepstrath.bank.repository.TransactionRepository;
import uk.co.asepstrath.bank.service.AnalyticsService;
import uk.co.asepstrath.bank.service.PriceService;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnalyticsServiceTest {
    private TransactionRepository transactionRepository;
    private PriceRepository priceRepository;
    private TransactionService transactionService;
    private PriceService priceService;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp () {
        transactionRepository = mock(TransactionRepository.class);
        priceRepository = mock(PriceRepository.class);
        transactionService = new TransactionService(transactionRepository);
        priceService = new PriceService(priceRepository);
        analyticsService = new AnalyticsService(transactionService, priceService);
    }

    @Test
    void getPositionOneTransaction () {
        when(transactionService.getTransactionsByInvestor("1")).thenReturn(List.of (new Transaction("1", "10.00", "BUY", "1", "AAPL", 1500.99)));
        when(priceService.getPriceAtTimestamp("10.00", "AAPL")).thenReturn(150.0);
        when(priceService.getLatestPrice("AAPL")).thenReturn(150.0);
        List<Position> positions = analyticsService.getPositions("1");
        assertEquals("AAPL", positions.get(0).getTicker());
        assertEquals(10.01, positions.get(0).getNumberOfShares());
        assertEquals(150, positions.get(0).getBuyPrice());
        assertEquals(150.0, positions.get(0).getLastPrice());
        assertEquals(1500.99, positions.get(0).getBuyCashAmount());
        assertEquals(1500.99, positions.get(0).getCurrentCashAmount());
        assertEquals(0, positions.get(0).getPnl());
        assertEquals(0, positions.get(0).getPnlPercentage());
        assertEquals(0, positions.get(0).getRealisedPnl());
        assertEquals(1500.99, positions.get(0).getTotalInvested());
    }

    @Test
    void getPositionMultipleTransactions () {
        when(transactionService.getTransactionsByInvestor("1")).thenReturn(List.of (new Transaction("1", "10.00", "BUY", "1", "AAPL", 1500.99), new Transaction("2", "10.15", "BUY", "1", "AAPL", 100.0)));
        when(priceService.getPriceAtTimestamp("10.00", "AAPL")).thenReturn(150.0);
        when(priceService.getPriceAtTimestamp("10.15", "AAPL")).thenReturn(100.0);
        when(priceService.getLatestPrice("AAPL")).thenReturn(100.0);
        List<Position> positions = analyticsService.getPositions("1");
        assertEquals("AAPL", positions.get(0).getTicker());
        assertEquals(11.01, positions.get(0).getNumberOfShares());
        assertEquals(145.46, positions.get(0).getBuyPrice());
        assertEquals(100.0, positions.get(0).getLastPrice());
        assertEquals(1600.99, positions.get(0).getBuyCashAmount());
        assertEquals(1100.66, positions.get(0).getCurrentCashAmount());
        assertEquals(-500.33, positions.get(0).getPnl());
        assertEquals(-31.25, positions.get(0).getPnlPercentage());
        assertEquals(0, positions.get(0).getRealisedPnl());
        assertEquals(1600.99, positions.get(0).getTotalInvested());
    }

    @Test
    void getPositionBuySell () {
        when(transactionService.getTransactionsByInvestor("1")).thenReturn(List.of (new Transaction("1", "10.00", "BUY", "1", "AAPL", 1500.99), new Transaction("2", "10.15", "SELL", "1", "AAPL", 100.0)));
        when(priceService.getPriceAtTimestamp("10.00", "AAPL")).thenReturn(150.0);
        when(priceService.getPriceAtTimestamp("10.15", "AAPL")).thenReturn(100.0);
        when(priceService.getLatestPrice("AAPL")).thenReturn(100.0);
        List<Position> positions = analyticsService.getPositions("1");
        assertEquals("AAPL", positions.get(0).getTicker());
        assertEquals(9.01, positions.get(0).getNumberOfShares());
        assertEquals(150, positions.get(0).getBuyPrice());
        assertEquals(100.0, positions.get(0).getLastPrice());
        assertEquals(1350.99, positions.get(0).getBuyCashAmount());
        assertEquals(900.66, positions.get(0).getCurrentCashAmount());
        assertEquals(-450.33, positions.get(0).getPnl());
        assertEquals(-33.33, positions.get(0).getPnlPercentage());
        assertEquals(-50, positions.get(0).getRealisedPnl());
        assertEquals(1500.99, positions.get(0).getTotalInvested());
    }
}
