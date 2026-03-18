package uk.co.asepstrath.bank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;

import static org.junit.jupiter.api.Assertions.*;

class AuthorisationTest {

    Authorisation a;

    @BeforeEach
    void setup(){
        Investor i = new Investor("JF2020","Jimmy Fitzsimmons", 123000);
        a = new Authorisation(i);
    }

    void constructorParamless(){
        a = new Authorisation(null);
        assertEquals(new Authorisation(null), a);
    }

    @Test
    void isAuthorisedWithInvestor() {
        assertEquals(true,a.isAuthorised());
    }

    @Test
    void isAuthorisedWithoutInvestor(){
        assertEquals(false, new Authorisation(null));
    }
}