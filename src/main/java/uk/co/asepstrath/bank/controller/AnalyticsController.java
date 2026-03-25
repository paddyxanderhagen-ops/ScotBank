package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.StatusCode;
import io.jooby.annotation.GET;
import io.jooby.annotation.POST;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Position;
import uk.co.asepstrath.bank.repository.PriceRepository;
import uk.co.asepstrath.bank.service.APIService;
import uk.co.asepstrath.bank.service.AnalyticsService;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Path("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final APIService apiService;
    private final PriceRepository priceRepository;
    private String timestamp;

    public AnalyticsController(AnalyticsService analyticsService, APIService apiService, PriceRepository priceRepository) {
        this.analyticsService = analyticsService;
        this.apiService = apiService;
        this.priceRepository = priceRepository;

    }


    @GET
    public ModelAndView<Map<String, Object>> positions(Context ctx) {
        Map<String, Object> map = new HashMap<>();
        List<Position> positions = getSession(ctx);
        if (positions == null) {
            return null;
        }

        positions.sort(Comparator.comparingDouble(Position::getPnl).reversed());

        double totalValue = positions.stream().mapToDouble(Position::getCurrentValue).sum();
        double totalPnl = positions.stream().mapToDouble(Position::getPnl).sum();
        double totalRealisedPnl = positions.stream().mapToDouble(Position::getRealisedPnl).sum();
        double totalInvested = positions.stream().mapToDouble(Position::getTotalInvested).sum();

        timestamp = "";


        getLastUpdatedTime();

        map.put("positions", positions);
        map.put("totalValue", Math.round(totalValue * 100.0) / 100.0);
        map.put("totalPnl", Math.round(totalPnl * 100.0) / 100.0);
        map.put("totalRealisedPnl", Math.round(totalRealisedPnl * 100.0) / 100.0);
        map.put("totalInvested", Math.round(totalInvested * 100.0) / 100.0);
        map.put("lastUpdated", timestamp);

        return new ModelAndView<>("positions.hbs", map);
    }

    @GET("/{ticker}")
    public ModelAndView<Map<String, Object>> position(Context ctx, @PathParam String ticker) {
        List<Position> positions = getSession(ctx);
        if (positions == null) {
            return null;
        }

        String investorId = ctx.session().get("id").value();
        Map<String, Object> map = new HashMap<>();

        Position position = analyticsService.getPosition(ticker, investorId);

        map.put("position", position);

        return new ModelAndView<>("position.hbs", map);
    }

    @POST("/refresh")
    public void refreshPrices(Context ctx) {
        List<Position> positions = getSession(ctx);
        if (positions == null) {
            return;
        }

        Set<String> tickers = positions.stream()
                .map(Position::getTicker)
                .collect(Collectors.toSet());

        String token = apiService.loadToken();

        tickers.parallelStream().forEach(ticker -> {
            try {
                apiService.getAllPrices(ticker, priceRepository, token);
            } catch (Exception e) {
                System.err.println("Failed: " + ticker + " " + e);
            }
        });

        getLastUpdatedTime();
    }

    private void getLastUpdatedTime() {
        try {
            timestamp = priceRepository.getLastTime();

            if (timestamp == null || timestamp.isBlank()) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Prices not found");
            }
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve prices");
        }
    }

    private List<Position> getSession(Context ctx) {
        if (ctx == null) {
            return null;
        }

        Session session = ctx.session();

        List<Position> positions = null;

        if (!session.get("id").isMissing()) {
            String id = session.get("id").value();
            positions = analyticsService.getPositions(id);
        }

        if (positions == null) {
            ctx.sendRedirect("/login");
            return null;
        }
        return positions;
    }
}

