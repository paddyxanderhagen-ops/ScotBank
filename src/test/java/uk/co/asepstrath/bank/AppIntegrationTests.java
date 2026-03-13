package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AppBootTest {

    @Test
    void appShouldStartAndStopCleanly() {
        assertDoesNotThrow(() -> {
            App app = new App();
            app.onStart();
            app.onStop();
        });
    }
}