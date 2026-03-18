package uk.co.asepstrath.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InvestorTest {

    Investor i;

    @BeforeEach
    void setup(){
        i = new Investor("BB456","Billy Biggins",367.49);
    }

    @Test
    void constructorParamless(){
        i = new Investor();
        assertEquals("0",i.getId());
        assertEquals("",i.getName());
        assertEquals(0.0,i.getCashBalance());
        assertEquals(new ArrayList<>(),i.getStocks());
    }

    @Test
    void constructorParams(){
        assertEquals("BB456",i.getId());
        assertEquals("Billy Biggins",i.getName());
        assertEquals(367.49,i.getCashBalance());
        assertEquals(new ArrayList<>(),i.getStocks());
    }

    @Test
    void getId() {
        assertEquals("BB456",i.getId());
    }

    @Test
    void setId() {
        i.setId("CD123");
        assertEquals("CD123",i.getId());
    }

    @Test
    void getName() {
        assertEquals("Billy Biggins",i.getName());
    }

    @Test
    void setName() {
        i.setName("Claude Dembele");
        assertEquals("Claude Dembele",i.getName());
    }

    @Test
    void getCashBalance() {
        assertEquals(367.49,i.getCashBalance());
    }

    @Test
    void setCashBalance() {
        i.setCashBalance(500.00);
        assertEquals(500.00,i.getCashBalance());
    }

    @Test
    void getStocks() {
        assertEquals(new ArrayList<>(),i.getStocks());
    }

    @Test
    void setStocks() {
        Stock s1 = new Stock();
        Stock s2 = new Stock("META",500,55.5,true);
        ArrayList<Stock> l = new ArrayList<>();
        l.add(s1);
        l.add(s2);
        assertEquals(2,i.getStocks().size());
    }
}