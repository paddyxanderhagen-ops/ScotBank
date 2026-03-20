package uk.co.asepstrath.bank.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Etf;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EtfDTOTest {

    EtfDTO eDTO;

    @BeforeEach
    void setup(){
        eDTO = new EtfDTO();
    }

    @Test
    void getEtfs() {
        assertEquals(null,eDTO.getEtfs());
    }
}