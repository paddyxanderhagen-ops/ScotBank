package uk.co.asepstrath.bank.integration.controllers;

import io.jooby.StatusCode;
import io.jooby.test.JoobyTest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.TestApp;
import uk.co.asepstrath.bank.service.APIService;

import static org.junit.jupiter.api.Assertions.*;

@JoobyTest(TestApp.class)
public class PortfolioCategorisationControllerTest {

    private String id;

    @BeforeEach
    void setUp() {
        APIService service = new APIService();
        id = service.getAllInvestor().getInvestors().getFirst().getId();
        Unirest.config().reset();
        Unirest.config().followRedirects(false);
    }

    @Test
    void getPortfolioCategorisationNoSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "portfolio-categorisation")
                .asString();
        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void getPortfolioCategorisationNoSessionRedirectsToLogin(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "portfolio-categorisation")
                .asString();
        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    @Test
    void getPortfolioCategorisationValidSessionReturns200(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");
        HttpResponse<String> response = Unirest.get(serverPath + "portfolio-categorisation")
                .header("Cookie", cookie)
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void getPortfolioCategorisationReturnsHtml(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");
        HttpResponse<String> response = Unirest.get(serverPath + "portfolio-categorisation")
                .header("Cookie", cookie)
                .asString();
        assertTrue(response.getHeaders().getFirst("Content-Type").contains("text/html"));
    }

    @Test
    void getPortfolioNoSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "api/portfolio")
                .asString();
        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void getPortfolioValidSessionReturns200(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");
        HttpResponse<String> response = Unirest.get(serverPath + "api/portfolio")
                .header("Cookie", cookie)
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void getPortfolioValidSessionReturnsJson(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");
        HttpResponse<String> response = Unirest.get(serverPath + "api/portfolio")
                .header("Cookie", cookie)
                .asString();
        assertTrue(response.getHeaders().getFirst("Content-Type").contains("application/json"));
    }

    @Test
    void getPortfolioValidSessionContainsCashAndHoldings(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");
        HttpResponse<String> response = Unirest.get(serverPath + "api/portfolio")
                .header("Cookie", cookie)
                .asString();
        assertTrue(response.getBody().contains("cash"));
        assertTrue(response.getBody().contains("holdings"));
    }

    @Test
    void getEquitiesMetadataWithTickerReturns200(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "api/equities/metadata")
                .queryString("tickers", "AAPL")
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void getEquitiesMetadataReturnsJson(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "api/equities/metadata")
                .queryString("tickers", "AAPL")
                .asString();
        assertTrue(response.getHeaders().getFirst("Content-Type").contains("application/json"));
    }

    @Test
    void getEquitiesMetadataNoTickerReturns200(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "api/equities/metadata")
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    private HttpResponse<String> login(String serverPath) {
        return Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();
    }
}