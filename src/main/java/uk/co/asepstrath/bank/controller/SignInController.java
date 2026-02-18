package uk.co.asepstrath.bank.controller;

import io.jooby.ModelAndView;
import io.jooby.annotation.FormParam;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;

import java.util.HashMap;
import java.util.Map;

@Path("/login")
public class SignInController {

    @GET
    public ModelAndView<Map<String, String>> login () {
        Map<String, String> model = new HashMap<>();
        return new ModelAndView<>("login.hbs", model);
    }

    @POST
    public String post (@FormParam String email, @FormParam String name) {
        return "Name: " + name + " \nEmail: " + email;
    }
}
