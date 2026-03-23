package uk.co.asepstrath.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxReportTest {

    TaxReport tr;
    ArrayList<TaxSaleLine> sales;
    TaxSaleLine tsl1;
    TaxSaleLine tsl2;
    TaxSaleLine tsl3;
    TaxSaleLine tsl4;

    @BeforeEach
    void setup() {
        ArrayList<TaxSaleLine> sales = new ArrayList<>();
        tsl1 = new TaxSaleLine("TSLA", "12:30:30", 0, 12345.02);
        tsl2 = new TaxSaleLine("NEWT", "15:14:02", 22222.22, 11111.11);
        tsl3 = new TaxSaleLine("ALBE", "09:30:00", 33333.33, 11111.11);
        tsl4 = new TaxSaleLine("GALI", "21:00:15", 55555.55, 11111.11);
        sales.add(tsl1);
        sales.add(tsl2);
        sales.add(tsl3);
        sales.add(tsl4);
        tr = new TaxReport(2016, sales);
    }

    @Test
    void getYear() {
        assertEquals(2016, tr.getYear());
    }

    @Test
    void getSales() {
        assertEquals(4, tr.getSales().size());
        assertEquals("TSLA", tr.getSales().get(0).getTicker());
        assertEquals("NEWT", tr.getSales().get(1).getTicker());
    }

    @Test
    void getTotalGains() {
        assertEquals(77777.77, tr.getTotalGains());
    }

    @Test
    void getTotalLosses() {
        assertEquals(-12345.02, tr.getTotalLosses());
    }

    @Test
    void getNetGains() {
        assertEquals(65432.75, tr.getNetGains());
    }

    @Test
    void getAllowance() {
        assertEquals(3000, tr.getAllowance());
    }

    @Test
    void getTaxableGains() {
        assertEquals(62432.75, tr.getTaxableGains());
    }

    @Test
    void getTaxRate() {
        assertEquals(0.24, tr.getTaxRate());
    }

    @Test
    void getTaxOwed() {
        assertEquals(14983.86, tr.getTaxOwed());
    }

    @Test
    void getAllowanceUsed() {
        assertEquals(3000, tr.getAllowanceUsed());
    }

    @Test
    void getAllowanceRemaining() {
        assertEquals(0, tr.getAllowanceRemaining());
    }
}