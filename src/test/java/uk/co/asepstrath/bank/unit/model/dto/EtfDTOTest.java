package uk.co.asepstrath.bank.unit.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.dto.EtfDTO;

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