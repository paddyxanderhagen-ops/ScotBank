package uk.co.asepstrath.bank.unit.model;

import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Pagination;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaginationTest {
    @Test
    void getNextToken() {
        Pagination p = new Pagination();
        p.setNextToken("abcde");
        assertEquals("abcde", p.getNextToken());
    }

    @Test
    void setNextToken() {
        Pagination p = new Pagination();
        p.setNextToken("test string");
        assertEquals("test string", p.getNextToken());
    }
}