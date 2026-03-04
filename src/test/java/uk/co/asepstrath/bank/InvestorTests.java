package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;

import static org.junit.jupiter.api.Assertions.*;

public class InvestorTests {

    @Test
    void newInvestor_NoParams(){
        Investor i = new Investor();
        assertNotNull(i);
    }

    @Test
    void newInvestor_NoParams_Attributes(){
        Investor i = new Investor();
        assertEquals("0", i.getId(),"Investor objects created with parameterless constructor should have id '0'.");
        assertEquals(0.0, i.getCashBalance(),"Investor objects made with parameterless constructor should have 0.0 cash balance.");
        assertEquals("", i.getName(),"Investor objects made with parameterless constructor should have empty string as name.");
    }

    @Test
    void Investor_getID(){
        Investor i = new Investor("ABCD","",0.0);
        assertEquals("ABCD", i.getId(),"getID should return the correct ID.");
    }

    @Test
    void Investor_getName(){
        Investor i = new Investor("","Sally Cinnamon",0.0);
        assertEquals("Sally Cinnamon", i.getName(),"getName should return the correct name.");
    }

    @Test
    void Investor_getCashBalance(){
        Investor i = new Investor("","",50.0);
        assertEquals(50.0, i.getCashBalance(),"getCashBalance should return correct balance.");
    }

    @Test
    void newInvestor_WithParameters(){
        Investor i = new Investor("ABCD1234","Johnny Dimblebee",100000);
        assertEquals("ABCD1234", i.getId(),"All investor details should be as intended after construction.");
        assertEquals("Johnny Dimblebee", i.getName(),"All investor details should be as intended after construction.");
        assertEquals(100000, i.getCashBalance(),"All investor details should be as intended after construction.");
    }

    @Test
    void Investor_setID(){
        Investor i = new Investor();
        i.setId("ZYXV4321");
        assertEquals(i.getId(),"ZXYV4321","setID should correctly set the ID to the provided value.");
    }

    @Test
    void Investor_setName(){
        Investor i = new Investor();
        i.setName("Captain Morgan");
        assertEquals(i.getName(),"Captain Morgan","setName should correctly set the name to the provided value.");
    }

    @Test
    void Investor_setCashBalance(){
        Investor i = new Investor();
        i.setCashBalance(20000);
        assertEquals(20000,i.getCashBalance(),"setCashBalance should set the CashBalance to the correct value.");
    }

}