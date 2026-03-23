package uk.co.asepstrath.bank.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Constituent;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstituentTest {

    Constituent c;

    @BeforeEach
    void setup() {
        c = new Constituent("META", 2.35);
    }

    @Test
    void constructorParamless() {
        c = new Constituent();
        assertEquals("", c.getTicker());
        assertEquals(0, c.getWeight());
    }

    @Test
    void getTicker() {
        assertEquals("META", c.getTicker());
    }

    @Test
    void setTicker() {
        c.setTicker("FCBK");
        assertEquals("FCBK", c.getTicker());
    }

    @Test
    void getWeight() {
        assertEquals(2.35, c.getWeight());
    }

    @Test
    void setWeight() {
        c.setWeight(33333.33);
        assertEquals(33333.33, c.getWeight());
    }
}