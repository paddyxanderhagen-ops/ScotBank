package uk.co.asepstrath.bank.integration;

import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.service.APIService;

import static org.junit.jupiter.api.Assertions.*;

public class APIServiceTest {

    @Test
    void getAllInvestors () {
        APIService service = new APIService();
        InvestorDTO result = service.getAllInvestor();

        assertNotNull(result);
        assertFalse(result.getInvestors().isEmpty());
    }

    @Test
    void getAllTransactions () {
        APIService service = new APIService();
        TransactionDTO result = service.getAllTransaction();

        assertNotNull(result);
        assertFalse(result.getTransactions().isEmpty());
    }
}
