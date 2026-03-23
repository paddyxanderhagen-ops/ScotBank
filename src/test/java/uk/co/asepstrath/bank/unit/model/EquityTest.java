package uk.co.asepstrath.bank.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Equity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EquityTest {

    Equity e;

    @BeforeEach
    void setup() {
        e = new Equity("Mocha Joes", "MCJO", "COFFEE");
    }

    @Test
    void constructorParamless() {
        e = new Equity();
        assertEquals("", e.getName());
        assertEquals("", e.getTicker());
        assertEquals("", e.getSector());
    }

    @Test
    void constructorParams() {
        assertEquals("Mocha Joes", e.getName());
        assertEquals("MCJO", e.getTicker());
        assertEquals("COFFEE", e.getSector());
    }

    @Test
    void getName() {
        assertEquals("Mocha Joes", e.getName());
    }

    @Test
    void setName() {
        e.setName("Latte Larrys");
        assertEquals("Latte Larrys", e.getName());
    }

    @Test
    void getTicker() {
        assertEquals("MCJO", e.getTicker());
    }

    @Test
    void setTicker() {
        e.setTicker("LALA");
        assertEquals("LALA", e.getTicker());
    }

    @Test
    void getSector() {
        assertEquals("COFFEE", e.getSector());
    }

    @Test
    void setSector() {
        e.setSector("Scones");
        assertEquals("Scones", e.getSector());
    }
}