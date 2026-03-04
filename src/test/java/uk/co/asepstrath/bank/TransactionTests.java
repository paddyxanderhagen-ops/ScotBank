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

    @Test
    void Transaction_getID(){
        Transaction t = new Transaction("ABCD1234","","","","",BigDecimal.valueOf(0.0));
        assertEquals(t.getId(),"ABCD1234","getID should return correct ID.");
    }

    @Test
    void Transaction_getTimeStamp(){
        Transaction t = new Transaction("","12:30","","","",BigDecimal.valueOf(0.0));
        assertEquals(t.getTimestamp(),"12:30","getTimeStamp should return the correct timestamp.");
    }

    @Test
    void Transaction_getType(){
        Transaction t = new Transaction("","","Withdraw","","",BigDecimal.valueOf(0.0));
        assertEquals(t.getType(),"Withdraw","getType should return the correct type.");
    }

    @Test
    void Transaction_getInvestorID(){
        Transaction t = new Transaction("","","","ABC123","",BigDecimal.valueOf(0.0));
        assertEquals(t.getInvestorId(),"ABC123","getInvestorID should return the correct investorID.");
    }

    @Test
    void Transaction_getTicker(){
        Transaction t = new Transaction("","","","","AMZN",BigDecimal.valueOf(0.0));
        assertEquals(t.getTicker(),"AMZN","getTicker should return the correct ticker.");
    }




}