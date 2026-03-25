package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.TaxReport;
import uk.co.asepstrath.bank.service.AnalyticsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/taxreport")
public class TaxReportController {

    private final AnalyticsService analyticsService;

    public TaxReportController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GET
    public ModelAndView<Map<String, Object>> taxReport(Context ctx, @QueryParam Integer year) {
        if (!isLoggedIn(ctx)) {
            ctx.sendRedirect("/login");
            return null;
        }
        String id = ctx.session().get("id").value();

        List<Integer> years = analyticsService.getAvailableYears(id);

        int selectedYear = 0;

        if (year != null) {
            selectedYear = year;
        } else if (years.isEmpty()) {
            selectedYear = 2025;
        } else {
            selectedYear = years.get(years.size() - 1);
        }

        TaxReport report = analyticsService.getTaxReport(id, selectedYear);

        Map<String, Object> map = new HashMap<>();
        map.put("report", report);
        map.put("years", years);
        map.put("selectedYear", selectedYear);

        return new ModelAndView<>("taxreport.hbs", map);
    }

    private boolean isLoggedIn(Context ctx) {
        if (ctx == null) return false;
        return !ctx.session().get("id").isMissing();
    }
}