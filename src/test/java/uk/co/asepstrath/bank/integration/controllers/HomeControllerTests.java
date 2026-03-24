package uk.co.asepstrath.bank.integration.controllers;

import io.jooby.StatusCode;
import io.jooby.test.JoobyTest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.TestApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(TestApp.class)
public class HomeControllerTests {

    @Test
    void landingReturns200 (String serverPath) {

    }

    @Test
    void contactReturns200 (String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", "TS2389")
                .asString();

        HttpResponse<String> response = Unirest.get(serverPath + "contact")
                .header("id", "TS2389")
                .asString();

        System.out.println(response.getHeaders());

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void dashboardReturns200 (String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", "TS2389")
                .asString();

        String cookie = login.getHeaders().getFirst("Set-Cookie");


        HttpResponse<String> response = Unirest.get(serverPath + "dashboard")
                .header("id", cookie)
                .asString();

        System.out.println(response.getHeaders());

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void cashManagementReturns200 (String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", "TS2389")
                .asString();

        String cookie = login.getHeaders().getFirst("Set-Cookie");


        HttpResponse<String> response = Unirest.get(serverPath + "cash-management")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }
}
