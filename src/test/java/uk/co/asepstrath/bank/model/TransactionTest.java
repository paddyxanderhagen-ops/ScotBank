package uk.co.asepstrath.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    Transaction t;

    @BeforeEach
    void setup(){
        t = new Transaction("12345","01-01-70","Withdrawal","Jonno453","GME",75400.12);
    }

    @Test
    void constructorParamless(){
        t = new Transaction();
        assertEquals("",t.getId());
        assertEquals("",t.getTimestamp());
        assertEquals("",t.getType());
        assertEquals("",t.getInvestorId());
        assertEquals(0,t.getTotalCashAmount());
    }

    @Test
    void constructorParams(){
        assertEquals("12345",t.getId());
        assertEquals("01-01-70",t.getTimestamp());
        assertEquals("Withdrawal",t.getType());
        assertEquals("Jonno453",t.getInvestorId());
        assertEquals(75400.12,t.getTotalCashAmount());
    }

    @Test
    void getId() {
        assertEquals("12345",t.getId());
    }

    @Test
    void setId() {
        t.setId("54321");
        assertEquals("54321",t.getId());
    }

    @Test
    void getTimestamp() {
        assertEquals("01-01-70",t.getTimestamp());
    }

    @Test
    void setTimestamp() {
        t.setTimestamp("18-03-26");
        assertEquals("18-03-26",t.getTimestamp());
    }

    @Test
    void getType() {
        assertEquals("Withdrawal",t.getType());
    }

    @Test
    void setType() {
        t.setType("Deposit");
        assertEquals("Deposit",t.getType());
    }

    @Test
    void getInvestorId() {
        assertEquals("Jonno453",t.getInvestorId());
    }

    @Test
    void setInvestorId() {
        t.setInvestorId("Johnald2007");
        assertEquals("Johnald2007",t.getInvestorId());
    }

    @Test
    void getTicker() {
        assertEquals("GME",t.getTicker());
    }

    @Test
    void setTicker() {
        t.setTicker("PLNTR");
        assertEquals("PLNTR",t.getTicker());
    }

    @Test
    void getTotalCashAmount() {
        assertEquals(75400.12,t.getTotalCashAmount());
    }

    @Test
    void setTotalCashAmount() {
        t.setTotalCashAmount(2345.45);
        assertEquals(2345.45,t.getTotalCashAmount());
    }
}