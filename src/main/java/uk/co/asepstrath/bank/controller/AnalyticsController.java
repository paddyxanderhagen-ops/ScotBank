package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.repository.PriceRepository;
import uk.co.asepstrath.bank.service.APIService;
import uk.co.asepstrath.bank.service.AnalyticsService;

import java.util.*;
import java.util.stream.Collectors;

@Path("/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;
    private APIService apiService;
    private PriceRepository priceRepository;

    public AnalyticsController(AnalyticsService analyticsService, APIService aPIService, PriceRepository priceRepository) {
        this.analyticsService = analyticsService;
        this.apiService = aPIService;
        this.priceRepository = priceRepository;

    }


    @GET
    public ModelAndView<Map<String,Object>> positions(Context ctx) {
        String investorId = ctx.session().get("id").value();

        List<Position> positions = analyticsService.getPositions(investorId);

        positions.sort(Comparator.comparingDouble(Position::getPnl).reversed());

        double totalValue = positions.stream().mapToDouble(Position::getCurrentCashAmount).sum();
        double totalPnl = positions.stream().mapToDouble(Position::getPnl).sum();
        double totalRealisedPnl = positions.stream().mapToDouble(Position::getRealisedPnl).sum();
        double totalInvested = positions.stream().mapToDouble(Position::getTotalInvested).sum();

        Map<String,Object> map = new HashMap<>();
        map.put("positions", positions);
        map.put("totalValue", Math.round(totalValue * 100.0) / 100.0);
        map.put("totalPnl", Math.round(totalPnl * 100.0) / 100.0);
        map.put("totalRealisedPnl", Math.round(totalRealisedPnl * 100.0) / 100.0);
        map.put("totalInvested", Math.round(totalInvested * 100.0) / 100.0);

        return new ModelAndView<>("positions.hbs", map);
    }


    @GET("/{ticker}")
    public ModelAndView<Map<String,Object>> position(Context context, @PathParam String ticker) {
        String investorId = context.session().get("id").value();
        Position position = analyticsService.getPosition(ticker, investorId);
        Map<String,Object> map = new HashMap<>();
        map.put("position", position);
        return new ModelAndView<>("position.hbs",map);
    }

    @POST("/refresh")
    public void refreshPrices(Context ctx) {
        String investorId = ctx.session().get("id").value();

        List<Position> positions = analyticsService.getPositions(investorId);
        Set<String> tickers = positions.stream()
                .map(Position::getTicker)
                .collect(Collectors.toSet());

        String token = apiService.loadToken();

        tickers.parallelStream().forEach(ticker -> {
            try {
                apiService.getAllPrices(ticker, priceRepository, token);
            } catch (Exception e) {
                System.err.println("Failed: " + ticker);
            }
        });
    }

}

