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
        assertEquals("ABCD1234", t.getId(),"getID should return correct ID.");
    }

    @Test
    void Transaction_getTimeStamp(){
        Transaction t = new Transaction("","12:30","","","",BigDecimal.valueOf(0.0));
        assertEquals("12:30", t.getTimestamp(),"getTimeStamp should return the correct timestamp.");
    }

    @Test
    void Transaction_getType(){
        Transaction t = new Transaction("","","Withdraw","","",BigDecimal.valueOf(0.0));
        assertEquals("Withdraw", t.getType(),"getType should return the correct type.");
    }

    @Test
    void Transaction_getInvestorID(){
        Transaction t = new Transaction("","","","ABC123","",BigDecimal.valueOf(0.0));
        assertEquals("ABC123", t.getInvestorId(),"getInvestorID should return the correct investorID.");
    }

    @Test
    void Transaction_getTicker(){
        Transaction t = new Transaction("","","","","AMZN",BigDecimal.valueOf(0.0));
        assertEquals("AMZN", t.getTicker(),"getTicker should return the correct ticker.");
    }

    @Test
    void Transaction_getTotalCashAmount(){
        Transaction t = new Transaction("","","","","",BigDecimal.valueOf(100.0));
        assertEquals(t.getTotalCashAmount(),BigDecimal.valueOf(100.0),"getTotalCashAmount should return the correct amount.");
    }

    @Test
    void Transaction_setID(){
        Transaction t = new Transaction();
        t.setId("MNOP5678");
        assertEquals("MNOP5678", t.getId(),"setID should correctly set ID using parameter.");
    }

    @Test
    void Transaction_setTimeStamp(){
        Transaction t = new Transaction();
        t.setTimestamp("20:00");
        assertEquals("20:00", t.getTimestamp(),"setTimeStamp should correctly set timestamp using the parameter.");
    }

    @Test
    void Transaction_setType(){
        Transaction t = new Transaction();
        t.setType("deposit");
        assertEquals("deposit", t.getType(),"setType should correctly set type using the parameter.");
    }

    @Test
    void Transaction_setInvestorID(){
        Transaction t = new Transaction();
        t.setInvestorId("NUMB8008");
        assertEquals("NUMB8008",t.getInvestorId(),"setInvestorID should correctly set InvestorID using the parameter.");
    }

    @Test
    void Transaction_setTotalCashAmount(){
        Transaction t = new Transaction();
        t.setTotalCashAmount(BigDecimal.valueOf(15000.0));
        assertEquals(BigDecimal.valueOf(15000.0),t.getTotalCashAmount(),"setTotalCashAmount should correctly set the amount using the parameter.");
    }



}