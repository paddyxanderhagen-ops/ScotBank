package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.StatusCode;
import io.jooby.value.Value;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import uk.co.asepstrath.bank.controller.PortfolioCategorisationController;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.service.AnalyticsService;
import uk.co.asepstrath.bank.service.InvestorService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioCategorisationControllerTest {

    private InvestorService investorService;
    private AnalyticsService analyticsService;
    private DataSource ds;
    private PortfolioCategorisationController controller;
    private Context ctx;
    private Session session;

    @BeforeEach
    void setUp() {
        investorService = mock(InvestorService.class);
        analyticsService = mock(AnalyticsService.class);
        ds = mock(DataSource.class);
        controller = new PortfolioCategorisationController(investorService, analyticsService, ds);
        ctx = mock(Context.class);
        session = mock(Session.class);
    }

    @Test
    void getPortfolioBreakdownReturnsView() {
        ModelAndView result = controller.getPortfolioBreakdown();

        assertNotNull(result);
        assertEquals("portfolio-categorisation.hbs", result.getView());
    }

    @Test
    void getPortfolioUnauthorized() throws Exception {
        Value value = mock(Value.class);

        when(ctx.session()).thenReturn(session);
        when(session.get("id")).thenReturn(value);
        when(value.isMissing()).thenReturn(true);

        String result = controller.getPortfolio(ctx);

        verify(ctx).setResponseCode(StatusCode.UNAUTHORIZED);
        assertEquals("{}", result);
    }

    @Test
    void getPortfolioSuccess() throws Exception {
        Value value = mock(Value.class);
        when(ctx.session()).thenReturn(session);
        when(session.get("id")).thenReturn(value);
        when(value.isMissing()).thenReturn(false);
        when(value.value()).thenReturn("inv-123");

        Investor investor = new Investor("inv-123", "John Doe", 1000.00);
        when(investorService.getInvestor("inv-123")).thenReturn(investor);

        Connection mockConn = mock(Connection.class);
        when(ds.getConnection()).thenReturn(mockConn);
        Statement mockStmt = mock(Statement.class);
        when(mockConn.createStatement()).thenReturn(mockStmt);
        ResultSet mockRs = mock(ResultSet.class);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true).thenReturn(false);
        when(mockRs.getDouble("total_cash_amount")).thenReturn(500.0);

        Position pos1 = new Position("AAPL", 10.0, 150.0, 155.0, 1500.0, 1550.0, 0.0, 1500.0);
        Position pos2 = new Position("GOOG", 5.0, 2000.0, 2100.0, 10000.0, 10500.0, 0.0, 10000.0);
        List<Position> positions = Arrays.asList(pos1, pos2);

        when(analyticsService.getPositions("inv-123")).thenReturn(positions);

        String result = controller.getPortfolio(ctx);

        verify(ctx).setResponseType("application/json");
        assertTrue(result.contains("\"cash\":1000.0")); // 1000 starting + 500 db
        assertTrue(result.contains("\"ticker\":\"AAPL\""));
        assertTrue(result.contains("\"shares\":10.0"));
        assertTrue(result.contains("\"ticker\":\"GOOG\""));
        assertTrue(result.contains("\"shares\":5.0"));
    }

    @Test
    void getPortfolioNoPositions() throws Exception {
        Value value = mock(Value.class);
        when(ctx.session()).thenReturn(session);
        when(session.get("id")).thenReturn(value);
        when(value.isMissing()).thenReturn(false);
        when(value.value()).thenReturn("inv-123");

        Investor investor = new Investor("inv-123", "John Doe", 1000.00);
        when(investorService.getInvestor("inv-123")).thenReturn(investor);

        Connection mockConn = mock(Connection.class);
        when(ds.getConnection()).thenReturn(mockConn);
        Statement mockStmt = mock(Statement.class);
        when(mockConn.createStatement()).thenReturn(mockStmt);
        ResultSet mockRs = mock(ResultSet.class);
        when(mockStmt.executeQuery(anyString())).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // DB returns empty

        when(analyticsService.getPositions("inv-123")).thenReturn(null);

        String result = controller.getPortfolio(ctx);

        verify(ctx).setResponseType("application/json");
        assertTrue(result.contains("\"cash\":1000.0"));
        assertTrue(result.contains("\"holdings\":[]"));
    }

    @Test
    void getEquitiesMetadata() {
        Value queryValue = mock(Value.class);
        when(ctx.query("tickers")).thenReturn(queryValue);
        when(queryValue.value("")).thenReturn("AAPL,GOOG");

        String mockJsonResponse = "{\"data\":[{\"ticker\":\"AAPL\",\"name\":\"Apple Inc.\"}]}";

        GetRequest mockGetRequest = mock(GetRequest.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);

        when(mockGetRequest.queryString("tickers", "AAPL,GOOG")).thenReturn(mockGetRequest);
        when(mockGetRequest.accept("application/json")).thenReturn(mockGetRequest);
        when(mockGetRequest.asString()).thenReturn(mockResponse);
        when(mockResponse.getBody()).thenReturn(mockJsonResponse);

        try (MockedStatic<Unirest> mockedUnirest = mockStatic(Unirest.class)) {
            mockedUnirest.when(() -> Unirest.get("https://api.asep-strath.co.uk/api/equities/metadata"))
                    .thenReturn(mockGetRequest);

            String result = controller.getEquitiesMetadata(ctx);

            verify(ctx).setResponseType("application/json");
            assertEquals(mockJsonResponse, result);
        }
    }
}