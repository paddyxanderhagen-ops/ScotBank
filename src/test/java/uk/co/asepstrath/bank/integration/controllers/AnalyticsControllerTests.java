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
class AnalyticsControllerTests {

    private String id;
    private String ticker;

    @BeforeEach
    void setUp() {
        APIService service = new APIService();
        id = service.getAllInvestor().getInvestors().getFirst().getId();
        ticker = service.getAllEquities().getEquities().getFirst().getTicker();
        Unirest.config().reset();
        Unirest.config().followRedirects(false);
    }

    @Test
    void analyticsValidSessionReturns200(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "analytics")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void analyticsReturnsHTML(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "analytics")
                .header("Cookie", cookie)
                .asString();

        assertTrue(response.getHeaders().getFirst("Content-Type").contains("text/html"));
    }

    @Test
    void analyticsRedirectsToLogin(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "analytics")
                .asString();

        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    @Test
    void analyticsInvalidSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "analytics")
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void analyticsPerTickerValidSessionReturns200(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "analytics/" + ticker)
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void analyticsPerTickerReturnsHTML(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "analytics/" + ticker)
                .header("Cookie", cookie)
                .asString();

        assertTrue(response.getHeaders().getFirst("Content-Type").contains("text/html"));
    }

    @Test
    void analyticsPerTickerRedirectsToLogin(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "analytics/" + ticker)
                .asString();

        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    @Test
    void analyticsPerTickerInvalidSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "analytics/" + ticker)
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void refreshValidSessionReturns200(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.post(serverPath + "analytics/refresh")
                .header("Cookie", cookie)
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void refreshInvalidSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "analytics/refresh")
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    private HttpResponse<String> login(String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();

        return login;
    }
}
