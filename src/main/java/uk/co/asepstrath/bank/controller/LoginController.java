package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.annotation.FormParam;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;

import java.util.HashMap;
import java.util.Map;


@Path("/login")
public class LoginController {

    @GET("")
    public ModelAndView<Map<String, Object>> loginPage() {
        return new ModelAndView<>("login.hbs", new HashMap<>());
    }

    @POST("")
    public void postLogin(@FormParam String id, Context ctx) {
        Session session = ctx.session();
        session.put("id", id);
        ctx.sendRedirect("/dashboard");
    }

}
