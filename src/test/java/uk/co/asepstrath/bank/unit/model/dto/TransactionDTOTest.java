package uk.co.asepstrath.bank.unit.model.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Pagination;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionDTOTest {

    List<Transaction> ts;
    Pagination p;
    Transaction t;
    TransactionDTO tDTO;

    @BeforeEach
    void setup() {
        ts = new ArrayList<Transaction>();
        t = new Transaction("12345", "01-01-70", "Withdrawal", "Jonno453", "GME", 75400.12);
        p = new Pagination();
        tDTO = new TransactionDTO();
        tDTO.addTransaction(t);
    }

    @Test
    void construcorParamless() {
        TransactionDTO tDTO = new TransactionDTO();
        assertEquals(new ArrayList<>(), tDTO.getTransactions());
        assertNull(tDTO.getPages());
    }

    @Test
    void getTransactions() {
        assertEquals(1, tDTO.getTransactions().size());
        assertEquals(t, tDTO.getTransactions().get(0));
    }

    @Test
    void getPages() {
        assertNull(tDTO.getPages());
    }

    @Test
    void addTransaction() {
        tDTO.addTransaction(t);

        List<Transaction> result = tDTO.getTransactions();
        assertEquals(2, result.size());
        assertEquals(t, result.get(0));
    }
}