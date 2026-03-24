package uk.co.asepstrath.bank.unit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Constituent;
import uk.co.asepstrath.bank.model.Etf;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EtfTest {

    Etf e;
    ArrayList<Constituent> a = new ArrayList<Constituent>();

    @BeforeEach
    void setup() {
        e = new Etf("PNGN", "Penguin", "Booksellers", "20-04-26", a);
    }

    @Test
    void constructorParamless() {
        e = new Etf();
        assertEquals("", e.getName());
        assertEquals("", e.getTicker());
        assertEquals("", e.getDescription());
        assertEquals("", e.getInceptionDate());
        assertEquals(a, e.getConstituents());
    }

    @Test
    void constuctorParams() {
        assertEquals("Penguin", e.getName());
        assertEquals("PNGN", e.getTicker());
        assertEquals("Booksellers", e.getDescription());
        assertEquals("20-04-26", e.getInceptionDate());
        assertEquals(a, e.getConstituents());
    }

    @Test
    void getName() {
        assertEquals("Penguin", e.getName());
    }

    @Test
    void setName() {
        e.setName("Hawk");
        assertEquals("Hawk", e.getName());
    }

    @Test
    void getTicker() {
        assertEquals("PNGN", e.getTicker());
    }

    @Test
    void setTicker() {
        e.setTicker("HAWK");
        assertEquals("HAWK", e.getTicker());
    }

    @Test
    void getDescription() {
        assertEquals("Booksellers", e.getDescription());
    }

    @Test
    void setDescription() {
        e.setDescription("Bird of prey");
        assertEquals("Bird of prey", e.getDescription());
    }

    @Test
    void getInceptionDate() {
        assertEquals("20-04-26", e.getInceptionDate());
    }

    @Test
    void setInceptionDate() {
        e.setInceptionDate("27-03-26");
        assertEquals("27-03-26", e.getInceptionDate());
    }

    @Test
    void getConstituents() {
        assertEquals(a, e.getConstituents());
    }

    @Test
    void setConstituents() {
        a.add(new Constituent());
        e.setConstituents(a);
        assertEquals(1, e.getConstituents().size());
    }
}