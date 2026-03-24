package uk.co.asepstrath.bank.unit.controller;

import io.jooby.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.InstrumentController;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.service.InstrumentService;
import uk.co.asepstrath.bank.service.PriceService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstrumentControllerTest {

    private InstrumentService instrumentService;
    private PriceService priceService;
    private InstrumentController controller;

    @BeforeEach
    void setUp() {
        instrumentService = mock(InstrumentService.class);
        priceService = mock(PriceService.class);
        controller = new InstrumentController(instrumentService, priceService);
    }

    @Test
    void investmentPage_returnsInstrumentsPage() {
        ModelAndView mv = controller.investmentPage();
        assertEquals("instruments.hbs", mv.getView());
        assertNotNull(mv.getModel());
        assertTrue(mv.getModel() instanceof Map);
    }

    @Test
    void investmentPage_searchEquity_returnsEquityView() throws Exception {
        Equity equity = new Equity("AAPL", "Apple Inc.", "Technology");
        when(instrumentService.getInstrument("AAPL")).thenReturn(equity);
        when(priceService.getLatestPrice("AAPL")).thenReturn(150.0);

        ModelAndView mv = controller.investmentPage("AAPL");

        assertEquals("equities.hbs", mv.getView());
        @SuppressWarnings("unchecked")
        Map<String, Object> model = (Map<String, Object>) mv.getModel();
        assertEquals(equity, model.get("equity"));
        assertEquals(150.0, model.get("price"));
    }

    @Test
    void investmentPage_searchEtf_returnsEtfView() throws Exception {
        Etf etf = new Etf("PNTR", "Palantir", "War", "2020-01-01", null);
        when(instrumentService.getInstrument("PNTR")).thenReturn(etf);
        when(priceService.getLatestPrice("PNTR")).thenReturn(200.0);

        ModelAndView mv = controller.investmentPage("PNTR");

        assertEquals("etfs.hbs", mv.getView());
        @SuppressWarnings("unchecked")
        Map<String, Object> model = (Map<String, Object>) mv.getModel();
        assertEquals(etf, model.get("etf"));
        assertEquals(200.0, model.get("price"));
    }

    @Test
    void investmentPage_searchInstrumentNotFound_returnsNotFoundView() {
        when(instrumentService.getInstrument("UNKNOWN")).thenReturn(null);

        ModelAndView mv = controller.investmentPage("UNKNOWN");

        assertEquals("instrument-not-found.hbs", mv.getView());
        assertNotNull(mv.getModel());
    }

    @Test
    void investmentPage_searchPriceServiceThrows_setsPriceNotAvailable() throws Exception {
        Equity equity = new Equity("AAPL", "Apple Inc.", "Technology");
        when(instrumentService.getInstrument("AAPL")).thenReturn(equity);
        when(priceService.getLatestPrice("AAPL")).thenThrow(new RuntimeException("DB down"));

        ModelAndView mv = controller.investmentPage("AAPL");

        assertEquals("equities.hbs", mv.getView());
        @SuppressWarnings("unchecked")
        Map<String, Object> model = (Map<String, Object>) mv.getModel();
        assertEquals("price not available", model.get("price"));
        assertEquals(equity, model.get("equity"));
    }
}