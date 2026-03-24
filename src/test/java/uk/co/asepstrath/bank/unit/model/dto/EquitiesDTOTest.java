package uk.co.asepstrath.bank.unit.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.dto.EquityDTO;

import static org.junit.jupiter.api.Assertions.assertNull;

class EquitiesDTOTest {

    EquityDTO eDTO;

    @BeforeEach
    void setup() {
        eDTO = new EquityDTO();
    }

    @Test
    void getEquities() {
        assertNull(eDTO.getEquities());
    }
}