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
class LoginControllerTest {

    private String id;

    @BeforeEach
    void setUp() {
        APIService service = new APIService();
        id = service.getAllInvestor().getInvestors().getFirst().getId();
        Unirest.config().reset();
        Unirest.config().followRedirects(false);
    }

    @Test
    void getLoginReturns200(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "login")
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void getLoginReturnsHtml(String serverPath) {
        HttpResponse<String> response = Unirest.get(serverPath + "login")
                .asString();
        assertTrue(response.getHeaders().getFirst("Content-Type").contains("text/html"));
    }

    @Test
    void postLoginWithValidIdReturns302(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();
        assertEquals(StatusCode.FOUND.value(), response.getStatus());
    }

    @Test
    void postLoginWithValidIdRedirectsToDashboard(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();
        assertEquals("/dashboard", response.getHeaders().getFirst("Location"));
    }

    @Test
    void postLoginWithValidIdSetsSessionCookie(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", id)
                .asString();
        assertNotNull(response.getHeaders().getFirst("Set-Cookie"));
    }

    @Test
    void postLoginWithInvalidIdReturns200(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", "1234323413123")
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void postLoginWithInvalidIdShowsError(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", "123123124512")
                .asString();
        assertTrue(response.getBody().contains("Investor not found"));
    }

    @Test
    void postLoginWithInvalidIdDoesNotSetCookie(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", "123123123")
                .asString();
        assertTrue(response.getHeaders().getFirst("Set-Cookie").isEmpty());
    }

    @Test
    void postLoginWithEmptyIdReturns200(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", "")
                .asString();
        assertEquals(StatusCode.OK.value(), response.getStatus());
    }

    @Test
    void postLoginWithEmptyIdShowsError(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", "")
                .asString();
        assertTrue(response.getBody().contains("Investor not found"));
    }

    @Test
    void postLoginWithEmptyIdDoesNotSetCookie(String serverPath) {
        HttpResponse<String> response = Unirest.post(serverPath + "login")
                .field("id", "")
                .asString();
        assertTrue(response.getHeaders().getFirst("Set-Cookie").isEmpty());
    }
}