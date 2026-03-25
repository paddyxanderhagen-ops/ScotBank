package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.co.asepstrath.bank.TransactionUtility;
import uk.co.asepstrath.bank.controller.TransactionController;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    private TransactionService transactionService;
    private TransactionController transactionController;
    private Context context;
    private Session session;
    private InvestorService investorService;

    @BeforeEach
    void setUp() {
        transactionService = Mockito.mock(TransactionService.class);
        investorService = Mockito.mock(InvestorService.class);
        context = Mockito.mock(Context.class);
        session = Mockito.mock(Session.class);

        transactionController = new TransactionController(transactionService, investorService);

        Value idValue = Mockito.mock(Value.class);
        when(idValue.isMissing()).thenReturn(false);
        when(idValue.value()).thenReturn("12345");
        when(session.get("id")).thenReturn(idValue);
        when(context.session()).thenReturn(session);
    }

    @Test
    void testGetTransactionsForInvestor() {
        List<Transaction> trans = new ArrayList<>();
        Value val = mock(Value.class);
        trans.add(new Transaction("5678", "10:27:00", "DEPOSIT", "Jacob67", "ROCK", 50.0));
        trans.add(new Transaction("1234", "12:30:00", "WITHDRAWAL", "Jacob67", "SHUP", 9.30));

        when(transactionService.getTransactionsByInvestor("Jacob67")).thenReturn(trans);
        when(investorService.getInvestor("Jacob67")).thenReturn(new Investor("Jacob67", "Jacob", 10000));
        when(context.session()).thenReturn(session);
        when(session.get("id")).thenReturn(val);
        when(val.isMissing()).thenReturn(false);
        when(val.value()).thenReturn("Jacob67");
        try (MockedStatic<TransactionUtility> mockedStatic = mockStatic(TransactionUtility.class)) {
            when(TransactionUtility.getExecuted(any(), eq(10000))).thenReturn(trans);
        }

        ModelAndView<Map<String, Object>> result = transactionController.getTransactionsForInvestor(context);

        Map<String, Object> model = result.getModel();

        List<Map<String, Object>> enriched = (List<Map<String, Object>>) model.get("transactions");



        assertNotNull(enriched);
        assertEquals(2, enriched.size());

        Map<String, Object> first = enriched.get(0);
        assertEquals("DEPOSIT", first.get("type"));
        assertEquals(50.0, first.get("amount"));
        assertEquals("10009.3", first.get("balanceAfter"));
        assertEquals("10:27:00", first.get("timestamp"));
        assertEquals("ROCK", first.get("ticker"));

        Map<String, Object> second = enriched.get(1);
        assertEquals("WITHDRAWAL", second.get("type"));
        assertEquals(9.30, second.get("amount"));
        assertEquals("10000.0", second.get("balanceAfter"));
        assertEquals("12:30:00", second.get("timestamp"));
        assertEquals("SHUP", second.get("ticker"));

        assertEquals(50.0, model.get("totalDeposits"));
        assertEquals(9.3, model.get("totalWithdrawals"));
        assertEquals("Jacob67", model.get("id"));
    }

    @Test
    void testNoTransactions() {
        when(transactionService.getTransactionsByInvestor("12345")).thenReturn(Collections.emptyList());
        when(investorService.getInvestor(anyString())).thenReturn(new Investor("1", "Test User", 10000));
        ModelAndView<Map<String, Object>> result = transactionController.getTransactionsForInvestor(context);

        Map<String, Object> model = result.getModel();
        List<Map<String, Object>> enrichedTransactions = (List<Map<String, Object>>) model.get("transactions");

        assertNotNull(enrichedTransactions);
        assertTrue(enrichedTransactions.isEmpty());
        assertEquals(0.0, model.get("totalDeposits"));
        assertEquals(0.0, model.get("totalWithdrawals"));
    }
}