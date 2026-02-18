package uk.co.asepstrath.bank.Home;

import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;

import java.util.HashMap;
import java.util.Map;

@Path("/")
public class HomeController {
    @GET
    public ModelAndView<Map<String, String>> index() {
        Map<String, String> model = new HashMap<>();
        model.put("username", "mykhailo123");
        return new ModelAndView<>("index.hbs", model);
    }
}
