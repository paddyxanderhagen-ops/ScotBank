package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.LoginController;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    Context ctx;
    Session sess;
    LoginController lc;
    InvestorService is;

    @BeforeEach
    void setup() {
        is = mock(InvestorService.class);
        lc = new LoginController(is);
        ctx = mock(Context.class);
        sess = mock(Session.class);
        when(ctx.session()).thenReturn(sess);
    }

    @Test
    void loginPage() {
        ModelAndView<Map<String, Object>> r = lc.loginPage();
        assertEquals("login.hbs", r.getView());
        assertNotNull(r.getModel());
    }

    @Test
    void postLogin() {
        String id = "Hailey45";
        when(is.getInvestor(id)).thenReturn(new Investor("Hailey45", "Hailey", 45));

        lc.postLogin(id, ctx);

        verify(sess).put("id", id);
        verify(ctx).sendRedirect("/dashboard");
    }
}