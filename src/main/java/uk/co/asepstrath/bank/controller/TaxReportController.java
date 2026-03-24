package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
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
        Session session = null;
        String id = null;
        if (ctx != null) {
            session = ctx.session();
        }
        if (session != null && !session.get("id").isMissing()) {
            id = session.get("id").value();
        }

        List<Integer> years = analyticsService.getAvailableYears(id);

        int selectedYear = (year != null) ? year : (years.isEmpty() ? 2025 : years.get(years.size() - 1));

        TaxReport report = analyticsService.getTaxReport(id, selectedYear);

        Map<String, Object> map = new HashMap<>();
        map.put("report", report);
        map.put("years", years);
        map.put("selectedYear", selectedYear);

        return new ModelAndView<>("taxreport.hbs", map);
    }
}