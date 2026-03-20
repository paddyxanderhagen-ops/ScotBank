package uk.co.asepstrath.bank.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Price;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PriceDTOTest {

    PriceDTO pDTO;

    @BeforeEach
    void setup(){
        pDTO = new PriceDTO();
    }

    @Test
    void getPrices() {
        assertEquals(null,pDTO.getPrices());
    }

    @Test
    void setPrices() {
        ArrayList<Price> a = new ArrayList<>();
        a.add(new Price());
        a.add(new Price());
        pDTO.setPrices(a);
        assertEquals(2, pDTO.getPrices().size());
    }

    @Test
    void getPages() {
        assertEquals(null,pDTO.getPages());
    }
}