package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.TransactionService;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TransactionServiceTests {

    private TransactionService ts;
    private List<Transaction> transList;
    private Transaction t1;

    @BeforeEach
    public void setup() throws Exception{
        ts = new TransactionService();

        t1 = new Transaction();
        t1.setId("123");
        t1.setTimestamp("12:00");
        t1.setType("Deposit");
        t1.setInvestorId("JB123");
        t1.setTicker("BNNA");
        t1.setTotalCashAmount(BigDecimal.valueOf(1000.0));

        transList = new ArrayList<>();
        transList.add(t1);
    }

    @Test
    void GetByID_CorrectDetails(){
        String result = ts.getById("JB123",transList);

        assertTrue(result.contains("id: 123"));
        assertTrue(result.contains("timestamp: 12:00"));
        assertTrue(result.contains("type: Deposit"));
        assertTrue(result.contains("investorId: JB123"));
        assertTrue(result.contains("ticker: BNNA"));
        assertTrue(result.contains("totalCashAmount: "+t1.getTotalCashAmount()));
    }

    @Test
    void testGetById_notFound() {
        String result = ts.getById("CD245", transList);

        assertEquals("Transaction:CD245 not found", result);
    }

    @Test
    void testGetById_emptyList() {
        String result = ts.getById("blahblah", new ArrayList<>());

        assertEquals("Transaction:blahblah not found", result);
    }



}