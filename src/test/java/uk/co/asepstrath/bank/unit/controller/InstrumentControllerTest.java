package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.Session;
import io.jooby.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.InstrumentController;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.service.InstrumentService;
import uk.co.asepstrath.bank.service.PriceService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InstrumentControllerTest {

    private InstrumentService instrumentService;
    private PriceService priceService;
    private InstrumentController controller;
    private Context ctx;
    private Session sess;

    @BeforeEach
    void setUp() {
        instrumentService = mock(InstrumentService.class);
        priceService = mock(PriceService.class);
        controller = new InstrumentController(instrumentService, priceService);
        ctx = mock(Context.class);
        sess = mock(Session.class);
    }

    @Test
    void investmentPage_returnsInstrumentsPage() {
        Value val = mock(Value.class);
        when(val.isMissing()).thenReturn(false);
        when(val.value()).thenReturn("12345");
        when(sess.get("id")).thenReturn(val);
        when(ctx.session()).thenReturn(sess);

        ModelAndView mv = controller.investmentPage(ctx);
        assertEquals("instruments.hbs", mv.getView());
        assertNotNull(mv.getModel());
        assertInstanceOf(Map.class, mv.getModel());
    }

    @Test
    void investmentPage_searchEquity_returnsEquityView() {
        Value val = mock(Value.class);
        when(val.isMissing()).thenReturn(false);
        when(val.value()).thenReturn("12345");
        when(sess.get("id")).thenReturn(val);
        when(ctx.session()).thenReturn(sess);

        Equity equity = new Equity("AAPL", "Apple Inc.", "Technology");
        when(instrumentService.getInstrument("AAPL")).thenReturn(equity);
        when(priceService.getLatestPrice("AAPL")).thenReturn(150.0);

        ModelAndView mv = controller.investmentPage("AAPL", ctx);

        assertEquals("equities.hbs", mv.getView());
        @SuppressWarnings("unchecked")
        Map<String, Object> model = (Map<String, Object>) mv.getModel();
        assertEquals(equity, model.get("equity"));
        assertEquals(150.0, model.get("price"));
    }

    @Test
    void investmentPage_searchEtf_returnsEtfView() {
        Value val = mock(Value.class);
        when(val.isMissing()).thenReturn(false);
        when(val.value()).thenReturn("12345");
        when(sess.get("id")).thenReturn(val);
        when(ctx.session()).thenReturn(sess);

        Etf etf = new Etf("PNTR", "Palantir", "War", "2020-01-01", null);
        when(instrumentService.getInstrument("PNTR")).thenReturn(etf);
        when(priceService.getLatestPrice("PNTR")).thenReturn(200.0);

        ModelAndView mv = controller.investmentPage("PNTR", ctx);

        assertEquals("etfs.hbs", mv.getView());
        @SuppressWarnings("unchecked")
        Map<String, Object> model = (Map<String, Object>) mv.getModel();
        assertEquals(etf, model.get("etf"));
        assertEquals(200.0, model.get("price"));
    }

    @Test
    void investmentPage_searchInstrumentNotFound_returnsNotFoundView() {
        Value val = mock(Value.class);
        when(val.isMissing()).thenReturn(false);
        when(val.value()).thenReturn("12345");
        when(sess.get("id")).thenReturn(val);
        when(ctx.session()).thenReturn(sess);

        when(instrumentService.getInstrument("UNKNOWN")).thenReturn(null);

        ModelAndView mv = controller.investmentPage("UNKNOWN", ctx);

        assertEquals("instrument-not-found.hbs", mv.getView());
        assertNotNull(mv.getModel());
    }

    @Test
    void investmentPage_searchPriceServiceThrows_setsPriceNotAvailable() {
        Value val = mock(Value.class);
        when(val.isMissing()).thenReturn(false);
        when(val.value()).thenReturn("12345");
        when(sess.get("id")).thenReturn(val);
        when(ctx.session()).thenReturn(sess);

        Equity equity = new Equity("AAPL", "Apple Inc.", "Technology");
        when(instrumentService.getInstrument("AAPL")).thenReturn(equity);
        when(priceService.getLatestPrice("AAPL")).thenThrow(new RuntimeException("DB down"));

        ModelAndView mv = controller.investmentPage("AAPL", ctx);

        assertEquals("equities.hbs", mv.getView());
        @SuppressWarnings("unchecked")
        Map<String, Object> model = (Map<String, Object>) mv.getModel();
        assertEquals("price not available", model.get("price"));
        assertEquals(equity, model.get("equity"));
    }
}