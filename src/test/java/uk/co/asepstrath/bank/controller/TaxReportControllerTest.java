package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.co.asepstrath.bank.model.TaxReport;
import uk.co.asepstrath.bank.service.AnalyticsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TaxReportControllerTest {

    private AnalyticsService analyticsService;
    private TaxReportController taxReportController;
    private Context mockContext;

    @BeforeEach
    void setUp() {
        analyticsService = Mockito.mock(AnalyticsService.class);
        mockContext = Mockito.mock(Context.class);
        taxReportController = new TaxReportController(analyticsService);

        Session mockSession = Mockito.mock(Session.class);
        Value mockValue = Mockito.mock(Value.class);

        when(mockContext.session()).thenReturn(mockSession);
        when(mockSession.get("id")).thenReturn(mockValue);
        when(mockValue.value()).thenReturn("12345");
    }

    @Test
    void testTaxReportWithYearProvided() {
        String investorId = "12345";
        Integer year = 2023;
        TaxReport taxReport = new TaxReport(2018, new ArrayList<>());
        List<Integer> years = Arrays.asList(2021, 2022, 2023);

        when(analyticsService.getAvailableYears(investorId)).thenReturn(years);
        when(analyticsService.getTaxReport(investorId, year)).thenReturn(taxReport);

        ModelAndView<Map<String, Object>> result = taxReportController.taxReport(mockContext, year);

        Map<String, Object> model = result.getModel();
        assertNotNull(model.get("report"));
        assertEquals(taxReport, model.get("report"));
        assertEquals(years, model.get("years"));
        assertEquals(year, model.get("selectedYear"));
    }

    @Test
    void testTaxReportWithDefaultYear() {
        String investorId = "12345";
        TaxReport taxReport = new TaxReport(2018, new ArrayList<>());
        List<Integer> years = Arrays.asList(2021, 2022);

        when(analyticsService.getAvailableYears(investorId)).thenReturn(years);
        when(analyticsService.getTaxReport(investorId, 2022)).thenReturn(taxReport);

        ModelAndView<Map<String, Object>> result = taxReportController.taxReport(mockContext, null);

        Map<String, Object> model = result.getModel();
        assertNotNull(model.get("report"));
        assertEquals(taxReport, model.get("report"));
        assertEquals(years, model.get("years"));
        assertEquals(2022, model.get("selectedYear"));
    }

    @Test
    void testTaxReportWithNoAvailableYears() {
        String investorId = "12345";
        TaxReport taxReport = new TaxReport(2018, new ArrayList<>());
        List<Integer> years = List.of();

        when(analyticsService.getAvailableYears(investorId)).thenReturn(years);
        when(analyticsService.getTaxReport(investorId, 2025)).thenReturn(taxReport);

        ModelAndView<Map<String, Object>> result = taxReportController.taxReport(mockContext, null);

        Map<String, Object> model = result.getModel();
        assertNotNull(model.get("report"));
        assertEquals(taxReport, model.get("report"));
        assertEquals(years, model.get("years"));
        assertEquals(2025, model.get("selectedYear"));
    }

//    @Test
//    void testTaxReportWithInvalidSession() {
//        when(mockContext.session().get("id").value()).thenReturn(null);
//
//        ModelAndView<Map<String, Object>> result = taxReportController.taxReport(mockContext, 2023);
//
//        Map<String, Object> model = result.getModel();
//        assertNull(model.get("report"));
//        assertNull(model.get("years"));
//        assertNull(model.get("selectedYear"));
//    }
}