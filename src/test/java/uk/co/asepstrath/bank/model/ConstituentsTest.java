package uk.co.asepstrath.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ConstituentsTest {

    Constituents c;

    @BeforeEach
    void setup(){
        c = new Constituents("META", BigDecimal.valueOf(2.35));
    }

    @Test
    void constructorParamless(){
        c = new Constituents();
        assertEquals("",c.getTicker());
        assertEquals(BigDecimal.ZERO,c.getWeight());
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
        assertEquals(BigDecimal.valueOf(2.35),c.getWeight());
    }

    @Test
    void setWeight() {
        c.setWeight(BigDecimal.valueOf(33333.33));
        assertEquals(BigDecimal.valueOf(33333.33),c.getWeight());
    }
}