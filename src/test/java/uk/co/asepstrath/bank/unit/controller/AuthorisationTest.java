package uk.co.asepstrath.bank.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.Authorisation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.service.InvestorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorisationTest {

    Authorisation a;
    InvestorRepository repo;

    @BeforeEach
    void setup() {
        Investor i = new Investor("JF2020", "Jimmy Fitzsimmons", 123000);
        repo = mock(InvestorRepository.class);
        a = new Authorisation(repo);
    }

    @Test
    void isAuthorisedWithInvestor() {
        InvestorService service = new InvestorService(repo);
        when(service.getInvestor("JF2020")).thenReturn(new Investor("JF2020", "Jimmy Fitzsimmons", 123000));
        assertTrue(a.isAuthorised("JF2020"));
    }

    @Test
    void isAuthorisedWithoutInvestor() {
        assertFalse(new Authorisation(repo).isAuthorised(anyString()));
    }
}