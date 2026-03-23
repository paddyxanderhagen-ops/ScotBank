package uk.co.asepstrath.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaxSaleLineTest {

    TaxSaleLine tsl;

    @BeforeEach
    void setup(){
        tsl = new TaxSaleLine("TSLA","12:30:30",54321.98,12345.02);
    }

    @Test
    void constructorParams(){
        assertEquals("TSLA",tsl.getTicker());
        assertEquals("12:30:30",tsl.getTimestamp());
        assertEquals(54321.98,tsl.getProceeds());
        assertEquals(12345.02,tsl.getCostBasis());
    }

    @Test
    void getTicker() {
        assertEquals("TSLA",tsl.getTicker());
    }

    @Test
    void getTimestamp() {
        assertEquals("12:30:30",tsl.getTimestamp());
    }

    @Test
    void getProceeds() {
        assertEquals(54321.98,tsl.getProceeds());
    }

    @Test
    void getCostBasis() {
        assertEquals(12345.02,tsl.getCostBasis());
    }

    @Test
    void roundsCorrectly(){
        tsl = new TaxSaleLine("","",0.643432,512.455);
        assertEquals(0.64,tsl.getProceeds());
        assertEquals(512.46,tsl.getCostBasis());
    }

    @Test
    void gainLossCorrect() {
        assertEquals(41976.96,tsl.getGainLoss());
    }

}