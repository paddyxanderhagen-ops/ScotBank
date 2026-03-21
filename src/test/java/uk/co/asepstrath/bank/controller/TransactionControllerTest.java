package uk.co.asepstrath.bank.controller;

import io.jooby.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionControllerTest {

    private TransactionService transactionService;
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        transactionService = Mockito.mock(TransactionService.class);
        transactionController = new TransactionController(transactionService);
    }

    @Test
    void testGetTransactionsForInvestor() {
        String investorId = "12345";

        List<Transaction> trans = new ArrayList<>();
        trans.add(new Transaction("1234","12:30:00","WITHDRAWAL","Jacob67","SHUP",9.30));
        trans.add(new Transaction("5678","10:27:00", "DEPOSIT", "Leon21","ROCK", 50.0));

        when(transactionService.getTransactionsByInvestor(investorId)).thenReturn(trans);

        ModelAndView<Map<String, Object>> result = transactionController.getTransactionsForInvestor(investorId, null);

        Map<String, Object> model = result.getModel();
        List<Map<String, Object>> enrichedTransactions = (List<Map<String, Object>>) model.get("transactions");

        assertNotNull(enrichedTransactions);
        assertEquals(2, enrichedTransactions.size());

        Map<String, Object> firstTransaction = enrichedTransactions.get(0);
        assertEquals("WITHDRAWAL", firstTransaction.get("type"));
        assertEquals(9.30, firstTransaction.get("amount"));
        assertEquals("9.3", firstTransaction.get("balanceAfter"));

        Map<String, Object> secondTransaction = enrichedTransactions.get(1);
        assertEquals("DEPOSIT", secondTransaction.get("type"));
        assertEquals(50.0, secondTransaction.get("amount"));
        assertEquals("50.0", secondTransaction.get("balanceAfter"));

        assertEquals(50.0, model.get("totalDeposits"));
        assertEquals(9.30, model.get("totalWithdrawals"));
    }

    @Test
    void testNoTransactions() {
        String investorId = "12345";

        when(transactionService.getTransactionsByInvestor(investorId)).thenReturn(Collections.emptyList());

        ModelAndView<Map<String, Object>> result = transactionController.getTransactionsForInvestor(investorId, null);

        Map<String, Object> model = result.getModel();
        List<Map<String, Object>> enrichedTransactions = (List<Map<String, Object>>) model.get("transactions");

        assertNotNull(enrichedTransactions);
        assertTrue(enrichedTransactions.isEmpty());
        assertEquals(0.0, model.get("totalDeposits"));
        assertEquals(0.0, model.get("totalWithdrawals"));
    }

    @Test
    void testInvalidInvestorId() {
        String investorId = "invalid_id";

        when(transactionService.getTransactionsByInvestor(investorId)).thenReturn(Collections.emptyList());

        ModelAndView<Map<String, Object>> result = transactionController.getTransactionsForInvestor(investorId, null);

        Map<String, Object> model = result.getModel();
        List<Map<String, Object>> enrichedTransactions = (List<Map<String, Object>>) model.get("transactions");

        assertNotNull(enrichedTransactions);
        assertTrue(enrichedTransactions.isEmpty());
        assertEquals(0.0, model.get("totalDeposits"));
        assertEquals(0.0, model.get("totalWithdrawals"));
    }




}