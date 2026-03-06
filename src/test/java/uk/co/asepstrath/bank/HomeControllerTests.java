package uk.co.asepstrath.bank;

import io.jooby.ModelAndView;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.HomeController;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HomeControllerTests {
    class FakeInvestorService extends InvestorService {
        @Override
        public Investor getInvestor(String id){
            Investor i = new Investor();
            i.setName("Sally");
            return i;
        }
        @Override
        public List<Investor> getAll(){
            return new ArrayList<>();
        }
    }

    @Test
    void userHomePageReturnsIndexPage(){
        HomeController controller = new HomeController(new FakeInvestorService());
        ModelAndView<Map<String,Object>> mv = controller.dashboardPage(null);

        assertEquals("index.hbs",mv.getView());
        assertEquals("Sally",mv.getModel().get("name"));
    }

    @Test
    void homePageUnauthorizedWhenIDNull(){
        HomeController controller = new HomeController(new FakeInvestorService());
        ModelAndView<Map<String,Object>> mv = controller.landingPage("", null);
        assertEquals("landingPage.hbs",mv.getView());
    }
}
