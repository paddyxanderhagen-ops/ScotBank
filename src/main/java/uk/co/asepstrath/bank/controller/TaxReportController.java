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

    private AnalyticsService analyticsService;

    public TaxReportController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GET
    public ModelAndView<Map<String, Object>> taxReport(Context ctx, @QueryParam Integer year) {
        String investorId = ctx.session().get("id").value();

        List<Integer> years = analyticsService.getAvailableYears(investorId);

        int selectedYear = (year != null) ? year : (years.isEmpty() ? 2025 : years.get(years.size() - 1));

        TaxReport report = analyticsService.getTaxReport(investorId, selectedYear);

        Map<String, Object> map = new HashMap<>();
        map.put("report", report);
        map.put("years", years);
        map.put("selectedYear", selectedYear);

        return new ModelAndView<>("taxreport.hbs", map);
    }
}