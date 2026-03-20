package uk.co.asepstrath.bank.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EquitiesDTOTest {

    EquitiesDTO eDTO;

    @BeforeEach
    void setup(){
        eDTO = new EquitiesDTO();
    }

    @Test
    void getEquities() {
        assertEquals(null, eDTO.getEquities());
    }
}