package uk.co.asepstrath.bank.integration.controllers;

import io.jooby.StatusCode;
import io.jooby.test.JoobyTest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.TestApp;
import uk.co.asepstrath.bank.service.APIService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JoobyTest(TestApp.class)
public class TaxReportControllerTests {

    private String id;

    @BeforeEach
    void setUp () {
        APIService service = new APIService();
        id = service.getAllInvestor().getInvestors().getFirst().getId();
        Unirest.config().reset();
        Unirest.config().followRedirects(false);
    }

    @Test
    void taxReportValidSessionReturns200(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "taxreport")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void taxReportValidSessionReturnsHTML(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "taxreport")
                .header("Cookie", cookie)
                .asString();

        assertTrue(response.getHeaders().getFirst("Content-Type").contains("text/html"));
    }

    @Test
    void taxReportInvalidSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "taxreport")
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void taxReportInvalidSessionRedirectsLogin(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "taxreport")
                .asString();

        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    private HttpResponse<String> login (String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();

        return login;
    }
}