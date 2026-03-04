package uk.co.asepstrath.bank;

import org.apiguardian.api.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.service.APIService;
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

    private static  TransactionService ts;
    private List<Transaction> transList;
    private Transaction t1;

    @BeforeAll
    public static void setup() {
        ts = new TransactionService();

        ts.createTransaction("123", "12:00", "Deposit", "JB123", "BNNA", 1000.0);
    }

    @Test
    void GetByID_CorrectDetails(){
        String result = ts.getById("123");

        assertTrue(result.contains("id: 123"));
        assertTrue(result.contains("timestamp: 12:00"));
        assertTrue(result.contains("type: Deposit"));
        assertTrue(result.contains("investorId: JB123"));
        assertTrue(result.contains("ticker: BNNA"));
        assertTrue(result.contains("totalCashAmount: 1000.0"));
    }

    @Test
    void testGetById_notFound() {
        String result = ts.getById("CD245");

        assertEquals("Transaction:CD245 not found", result);
    }
}