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
public class HomeControllerTest {

    private String id;

    @BeforeEach
    void setUp() {
        APIService service = new APIService();
        id = service.getAllInvestor().getInvestors().getFirst().getId();
        Unirest.config().reset();
        Unirest.config().followRedirects(false);
    }

    @Test
    void landingReturns200(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void contactReturns200(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "contact")
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void dashboardValidSessionReturns200(String serverPath) {
        HttpResponse<String> login = login(serverPath);

        String cookie = login.getHeaders().getFirst("Set-Cookie");


        HttpResponse<String> response = Unirest.get(serverPath + "dashboard")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void cashManagementValidSessionReturns200(String serverPath) {
        HttpResponse<String> login = login(serverPath);

        String cookie = login.getHeaders().getFirst("Set-Cookie");

        HttpResponse<String> response = Unirest.get(serverPath + "cash-management")
                .header("Cookie", cookie)
                .asString();

        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void dashboardInvalidSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "dashboard")
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void cashManagementInvalidSessionReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "cash-management")
                .asString();

        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void dashboardInvalidSessionRedirectsToLogin(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "dashboard")
                .asString();
        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    @Test
    void cashManagementInvalidSessionRedirectsToLogin(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "cash-management")
                .asString();
        assertEquals("/login", response.getHeaders().getFirst("Location"));
    }

    @Test
    void landingReturnsHtml(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath).asString();
        assertTrue(response.getHeaders().getFirst("Content-Type").contains("text/html"));
    }

    private HttpResponse<String> login(String serverPath) {
        HttpResponse<String> login = Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();

        return login;
    }
}
