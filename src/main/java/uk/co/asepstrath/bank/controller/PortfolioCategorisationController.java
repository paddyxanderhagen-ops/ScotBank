package uk.co.asepstrath.bank.controller;

import com.google.gson.Gson;
import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.StatusCode;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.repository.InstrumentRepository;
import uk.co.asepstrath.bank.service.AnalyticsService;
import uk.co.asepstrath.bank.service.InstrumentService;
import uk.co.asepstrath.bank.service.InvestorService;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
public class PortfolioCategorisationController {

    private final InvestorService investorService;
    private final AnalyticsService analyticsService;
    private final DataSource ds;


    public PortfolioCategorisationController(InvestorService investorService, AnalyticsService analyticsService, DataSource ds) {
        this.investorService = investorService;
        this.analyticsService = analyticsService;
        this.ds = ds;
    }

    @GET("/portfolio-categorisation")
    public ModelAndView getPortfolioBreakdown() {
        Map<String, Object> model = new HashMap<>();

        return new ModelAndView("portfolio-categorisation.hbs", model);
    }

    @GET("/api/portfolio")
    public String getPortfolio(Context ctx) throws Exception {
        Session session = ctx.session();
        if (session.get("id").isMissing()) {
            ctx.setResponseCode(StatusCode.UNAUTHORIZED);
            return "{}";
        }
        String id = session.get("id").value();

        Investor investor = investorService.getInvestor(id);
        CashAccountImplementation account = new CashAccountImplementation(investor, ds);
        InstrumentService instrumentService = new InstrumentService(new InstrumentRepository(ds));

        double cash = account.getBalance();
        List<Position> positions = analyticsService.getPositions(id);

        Map<String, Object> response = new HashMap<>();
        response.put("cash", cash);

        List<Map<String, Object>> holdings = new ArrayList<>();
        if (positions != null) {
            for (Position p : positions) {
                if (instrumentService.getInstrument(p.getTicker()) instanceof Equity) {
                    Map<String, Object> h = new HashMap<>();
                    h.put("ticker", p.getTicker());
                    h.put("shares", p.getShares());
                    h.put("currentPrice", p.getCurrPrice());
                    h.put("currentValue", p.getCurrentValue());
                    holdings.add(h);
                }
            }
        }

        response.put("holdings", holdings);

        Gson gson = new Gson();
        ctx.setResponseType("application/json");
        return gson.toJson(response);
    }

    @GET("/api/equities/metadata")
    public String getEquitiesMetadata(Context ctx) {
        String tickerName = ctx.query("tickers").value("");

        String response = Unirest.get("https://api.asep-strath.co.uk/api/equities/metadata")
                .queryString("tickers", tickerName)
                .accept("application/json")
                .asString().getBody();

        ctx.setResponseType("application/json");
        return response;
    }
}
