package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.HomeController;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


class HomeControllerTest {

    private HomeController hc;

    private InvestorService is;
    private TransactionService ts;
    private DataSource dsrc;

    private Context ctx;
    private Session sess;

    @BeforeEach
    void setup() {
        is = mock(InvestorService.class);
        ts = mock(TransactionService.class);
        dsrc = mock(DataSource.class);
        hc = new HomeController(is, dsrc, ts);
        ctx = mock(Context.class);
        sess = mock(Session.class);

        when(ctx.session()).thenReturn(sess);
    }

    @Test
    void landingPageNoIDReturnsView() {
        ModelAndView<Map<String, Object>> result = hc.landingPage(null, ctx);
        verify(sess).put("id", "");
        assertEquals("landingPage.hbs", result.getView());
    }

    @Test
    void dashboardPageAuthorisedReturnsView() throws SQLException {
        String id = "TS1505";
        Investor i = mock(Investor.class);

        Value mockValue = mock(Value.class);
        when(sess.get("id")).thenReturn(mockValue);
        when(mockValue.isMissing()).thenReturn(false); // or true if testing missing
        when(mockValue.value()).thenReturn(id);

        when(is.getInvestor(id)).thenReturn(i);
        when(i.getName()).thenReturn("Tupac");
        when(i.getId()).thenReturn(id);
        when(ts.getTransactionsByInvestor(id)).thenReturn(List.of());

        ModelAndView<Map<String, Object>> result = hc.dashboardPage(ctx);
        assertEquals("index.hbs", result.getView());
        assertEquals("Tupac", result.getModel().get("name"));
    }

    @Test
    void dashboardUnauthorisedRedirects() throws SQLException {
        String id = "Jambo243";

        Investor investor = mock(Investor.class);
        Value mockValue = mock(Value.class);
        when(sess.get("id")).thenReturn(mockValue);
        when(mockValue.isMissing()).thenReturn(false); // or true if testing missing
        when(mockValue.value()).thenReturn(id);

        when(is.getInvestor(id)).thenReturn(null);

        ModelAndView<Map<String, Object>> result = hc.dashboardPage(ctx);

        verify(ctx).sendRedirect("/login");
        assertNull(result);
    }
}