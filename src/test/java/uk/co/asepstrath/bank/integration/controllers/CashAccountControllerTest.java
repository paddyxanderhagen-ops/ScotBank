package uk.co.asepstrath.bank.integration.controllers;

import io.jooby.StatusCode;
import io.jooby.test.JoobyTest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.TestApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(TestApp.class)
class CashAccountControllerTest {

    @BeforeEach
    void setUp() {
        Unirest.config().reset();
        Unirest.config().followRedirects(false);
    }

    @Test
    void depositWithNoParamsReturns400(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/deposit")
                .asString();

        assertEquals(StatusCode.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void withdrawWithNoParamsReturns400(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/withdraw")
                .asString();

        assertEquals(StatusCode.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void depositWithoutSessionRedirects(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/deposit")
                .field("amount", 123.456)
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    @Test
    void withdrawWithoutSessionRedirects(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/withdraw")
                .field("amount", 123.456)
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }
}