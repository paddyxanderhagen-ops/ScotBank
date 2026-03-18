package uk.co.asepstrath.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    Stock stck;

    @BeforeEach
    void setup(){
        stck = new Stock("META",500,55.5,true);
    }

    @Test
    void constructorParamless(){
        Stock s = new Stock();
        assertEquals("",s.getTicker());
        assertEquals(0,s.getAmount());
        assertEquals(0,s.getPrice());
        assertEquals(false,s.isBuy());
    }

    @Test
    void constructorParams(){
        Stock s = new Stock("AMZN",20,7.85,true);
        assertEquals("AMZN",s.getTicker());
        assertEquals(20,s.getAmount());
        assertEquals(7.85,s.getPrice());
        assertEquals(true,s.isBuy());
    }

    @Test
    void getTicker() {
        assertEquals("META",stck.getTicker());
    }

    @Test
    void getAmount() {
        assertEquals(500,stck.getAmount());
    }

    @Test
    void getPrice() {
        assertEquals(55.5,stck.getPrice());
    }

    @Test
    void setTicker() {
        stck.setTicker("LINX");
        assertEquals("LINX",stck.getTicker());
    }

    @Test
    void setAmount() {
        stck.setAmount(250);
        assertEquals(250,stck.getAmount());
    }

    @Test
    void setPrice() {
        stck.setPrice(11.33);
        assertEquals(11.33,stck.getPrice());
    }

    @Test
    void isBuy() {
        assertEquals(true,stck.isBuy());
    }

    @Test
    void setBuy() {
        stck.setBuy(false);
        assertEquals(false,stck.isBuy());
    }
}