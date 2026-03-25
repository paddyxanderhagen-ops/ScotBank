package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

//    @Test
//    void testGetTransactionsForInvestor() {
//        List<Transaction> trans = new ArrayList<>();
//        Value val = mock(Value.class);
//        trans.add(new Transaction("5678", "10:27:00", "DEPOSIT", "Leon21", "ROCK", 50.0));
//        trans.add(new Transaction("1234", "12:30:00", "WITHDRAWAL", "Jacob67", "SHUP", 9.30));
//
//        when(transactionService.getTransactionsByInvestor("12345")).thenReturn(trans);
//        when(investorService.getInvestor(anyString())).thenReturn(new Investor("1", "Test User", 10000));
//        when(context.session()).thenReturn(session);
//        when(session.get("id")).thenReturn(val);
//        when(val.isMissing()).thenReturn(false);
//        when(val.value()).thenReturn("1234","5678");
//
//        mockStatic(TransactionUtility.class);
//        when(TransactionUtility.getExecuted(anyList(), anyDouble()))
//                .thenReturn(trans);
//
//        ModelAndView<Map<String, Object>> result = transactionController.getTransactionsForInvestor(context);
//
//        Map<String, Object> model = result.getModel();
//        List<Map<String, Object>> enrichedTransactions = (List<Map<String, Object>>) model.get("transactions");
//
//        assertNotNull(enrichedTransactions);
//        assertEquals(2, enrichedTransactions.size());
//
//        Map<String, Object> firstTransaction = enrichedTransactions.get(0);
//        assertEquals("WITHDRAWAL", firstTransaction.get("type"));
//        assertEquals(9.30, firstTransaction.get("amount"));
//        assertEquals("40.7", firstTransaction.get("balanceAfter"));
//
//        Map<String, Object> secondTransaction = enrichedTransactions.get(1);
//        assertEquals("DEPOSIT", secondTransaction.get("type"));
//        assertEquals(50.0, secondTransaction.get("amount"));
//        assertEquals("50.0", secondTransaction.get("balanceAfter"));
//
//
//
//        assertEquals(50.0, model.get("totalDeposits"));
//        assertEquals(9.3, model.get("totalWithdrawals"));
//    }

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