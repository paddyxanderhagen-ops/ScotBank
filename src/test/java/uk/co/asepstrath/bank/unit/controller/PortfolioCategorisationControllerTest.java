package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.StatusCode;
import io.jooby.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.PortfolioCategorisationController;
import uk.co.asepstrath.bank.service.AnalyticsService;
import uk.co.asepstrath.bank.service.InvestorService;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PortfolioCategorisationControllerTest {

    private InvestorService investorService;
    private AnalyticsService analyticsService;
    private DataSource ds;
    private PortfolioCategorisationController controller;

    @BeforeEach
    void setUp() {
        investorService = mock(InvestorService.class);
        analyticsService = mock(AnalyticsService.class);
        ds = mock(DataSource.class);
        controller = new PortfolioCategorisationController(investorService, analyticsService, ds);
    }

    @Test
    void getPortfolioBreakdownReturnsView() {
        ModelAndView result = controller.getPortfolioBreakdown();

        assertNotNull(result);
        assertEquals("portfolio-categorisation.hbs", result.getView());
    }

    @Test
    void getPortfolioUnauthorized() throws Exception {
        Context ctx = mock(Context.class);
        Session session = mock(Session.class);
        Value value = mock(Value.class);

        when(ctx.session()).thenReturn(session);
        when(session.get("id")).thenReturn(value);
        when(value.isMissing()).thenReturn(true);

        String result = controller.getPortfolio(ctx);

        verify(ctx).setResponseCode(StatusCode.UNAUTHORIZED);
        assertEquals("{}", result);
    }
}
