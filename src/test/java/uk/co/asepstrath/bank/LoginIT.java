package uk.co.asepstrath.bank;

import io.jooby.test.JoobyTest;
import org.junit.jupiter.api.Test;

@JoobyTest(App.class)
class LoginIT {

    @Test
    void shouldLoginSuccessfully() {

        given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", "John")
                .formParam("email", "john@email.com")
                .when()
                .post("/login")
                .then()
                .statusCode(200);
    }
}