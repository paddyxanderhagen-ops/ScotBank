package uk.co.asepstrath.bank.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Constituents;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ConstituentsTest {

    Constituents c;

    @BeforeEach
    void setup(){
        c = new Constituents("META", 2.35);
    }

    @Test
    void constructorParamless(){
        c = new Constituents();
        assertEquals("",c.getTicker());
        assertEquals(0 ,c.getWeight());
    }

    @Test
    void getTicker() {
        assertEquals("META",c.getTicker());
    }

    @Test
    void setTicker() {
        c.setTicker("FCBK");
        assertEquals("FCBK",c.getTicker());
    }

    @Test
    void getWeight() {
        assertEquals(2.35,c.getWeight());
    }

    @Test
    void setWeight() {
        c.setWeight(33333.33);
        assertEquals(33333.33,c.getWeight());
    }
}