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
public class TransactionControllerTest {

    String id;

    @BeforeEach
    void setUp () {
        APIService service = new APIService();
        id = service.getAllInvestor().getInvestors().getFirst().getId();
        Unirest.config().reset();
        Unirest.config().followRedirects(false);
    }

    @Test
    void getTransactionsReturns200(String serverPath) {
        HttpResponse<String> login = login(serverPath);

        String cookie = login.getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "transactions/view-all")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void postLoginReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "transactions/view-all")
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void getTransactionsRedirectsToLogin(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "transactions/view-all")
                .asString();
        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    @Test
    void getTransactionsResponseContainsExpectedFields(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "transactions/view-all")
                .header("Cookie", cookie)
                .asString();

        String body = response.getBody();
        assertTrue(body.contains("DEPOSIT") || body.contains("WITHDRAWAL") || body.contains("transactions"));
    }

    @Test
    void getTransactionsResponseContainsInvestorId(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "transactions/view-all")
                .header("Cookie", cookie)
                .asString();

        assertTrue(response.getBody().contains(id));
    }

    @Test
    void getTransactionsReturnsHtml(String serverPath) {
        String cookie = login(serverPath).getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "transactions/view-all")
                .header("Cookie", cookie)
                .asString();

        assertTrue(response.getHeaders().getFirst("Content-Type").contains("text/html"));
    }

    private HttpResponse<String> login (String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();

        return login;
    }
}
