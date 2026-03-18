package uk.co.asepstrath.bank.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.annotation.FormParam;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    Context ctx;
    Session sess;
    LoginController lc;

    @BeforeEach
    void setup(){
        lc = new LoginController();
        ctx = mock(Context.class);
        sess = mock(Session.class);
        when(ctx.session()).thenReturn(sess);
    }

    @Test
    void loginPage() {
        ModelAndView<Map<String,Object>> r = lc.loginPage();
        assertEquals("login.hbs",r.getView());
        assertNotNull(r.getModel());
    }

    @Test
    void postLogin() {
        String id = "Hailey45";

        lc.postLogin(id,ctx);

        verify(sess).put("id",id);
        verify(ctx).sendRedirect("/dashboard");



    }
}