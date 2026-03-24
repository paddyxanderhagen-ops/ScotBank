package uk.co.asepstrath.bank.integration.controllers;

import io.jooby.StatusCode;
import io.jooby.test.JoobyTest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.TestApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(TestApp.class)
public class CashAccountControllerTest {

    @Test
    void successfulDepositReturns200 (String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/deposit")
                .field("amount", 123.456)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void successfulWithdrawReturns200 (String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/withdraw")
                .field("amount", 123.456)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void depositWithNoParamsReturns400 (String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", "TS2389")
                .asString();

        String cookie = login.getHeaders().getFirst("Set-Cookie");


        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/deposit")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void withdrawWithNoParamsReturns400 (String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", "TS2389")
                .asString();

        String cookie = login.getHeaders().getFirst("Set-Cookie");


        HttpResponse<String> response = Unirest.post(serverPath + "dashboard/transaction/withdraw")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.BAD_REQUEST.value(), response.getStatus());
    }
}