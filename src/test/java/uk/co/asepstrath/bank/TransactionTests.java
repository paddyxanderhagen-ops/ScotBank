package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Transaction;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {

    @Test
    void newTransaction_NoParams(){
        Transaction t = new Transaction();
        assertNotNull(t,"A new object should be created");
    }

    @Test
    void newTransaction_Params(){
        Transaction t = new Transaction("12345","12:30","deposit","1234","AMZN", BigDecimal.valueOf(10.0));
        assertNotNull(t,"A new object should be created");
    }
}