package uk.co.asepstrath.bank.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InvestorDTOTest {

    Investor i1;
    Investor i2;
    Investor i3;
    InvestorDTO iDTO;

    @BeforeEach
    void setup(){
        i1 = new Investor();
        i2 = new Investor();
        i3 = new Investor();
        iDTO = new InvestorDTO();
    }

    @Test
    void constructorParamless(){
        assertEquals(new ArrayList<Investor>(), iDTO.getInvestors());
    }

    @Test
    void getInvestors() {
        iDTO.addInvestor(i1);
        iDTO.addInvestor(i2);
        iDTO.addInvestor(i3);
        assertEquals(3,iDTO.getInvestors().size());
    }

    @Test
    void addInvestor() {
        iDTO.addInvestor(i1);
        assertEquals(1,iDTO.getInvestors().size());
    }
}