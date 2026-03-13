package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InvestorServiceTests{
    private InvestorService s;
    private Investor i1;
    private Investor i2;

    @BeforeEach
    public void setup() throws Exception{
        s = new InvestorService();

        i1 = new Investor("JB123","John Biggins",400.0);
        i2 = new Investor("GP456","Gloria Prestwick",500.0);

        List<Investor> iList = Arrays.asList(i1,i2);

        Field field = InvestorService.class.getDeclaredField("investors");
        field.setAccessible(true);
        field.set(s, iList);
    }

    @Test
    public void getInvestorReturnsCorrectInvestor(){
        Investor test = s.getInvestor("JB123");
        assertEquals("JB123", test.getId());
    }

    @Test
    public void getInvestorReturnsNullAppropriately(){
        assertEquals(null, s.getInvestor("ZT678"));
    }

    @Test
    public void getInvestorReturnsAllOtherDetails(){
        Investor test = s.getInvestor("JB123");
        assertEquals("JB123", test.getId());
        assertEquals("John Biggins", test.getName());
        assertEquals(400.0,test.getCashBalance());
    }

}