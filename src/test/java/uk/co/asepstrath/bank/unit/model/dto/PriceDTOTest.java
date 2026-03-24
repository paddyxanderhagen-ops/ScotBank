package uk.co.asepstrath.bank.unit.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.dto.PriceDTO;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PriceDTOTest {

    PriceDTO pDTO;

    @BeforeEach
    void setup() {
        pDTO = new PriceDTO();
    }

    @Test
    void getPrices() {
        assertNull(pDTO.getPrices());
    }

    @Test
    void setPrices() {
        ArrayList<Price> a = new ArrayList<>();
        a.add(new Price("14:30", 2.30));
        a.add(new Price());
        pDTO.setPrices(a);
        assertEquals(2, pDTO.getPrices().size());
        assertEquals("14:30", pDTO.getPrices().get(0).getTimeStamp());
        assertEquals(2.30, pDTO.getPrices().get(0).getPrice());

    }

    @Test
    void getPages() {
        assertNull(pDTO.getPages());
    }
}