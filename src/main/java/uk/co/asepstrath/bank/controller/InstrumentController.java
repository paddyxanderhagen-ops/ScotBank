package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.Instrument;
import uk.co.asepstrath.bank.service.InstrumentService;
import uk.co.asepstrath.bank.service.PriceService;

import java.util.HashMap;
import java.util.Map;

@Path("/instruments")
public class InstrumentController {

    private final PriceService priceService;
    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService, PriceService priceService) {
        this.instrumentService = instrumentService;
        this.priceService = priceService;
    }

    @GET("")
    public ModelAndView<Map<String, Object>> investmentPage(Context ctx) {
        if (!isLoggedIn(ctx)) {
            ctx.sendRedirect("/login");
            return null;
        }

        return new ModelAndView<>("instruments.hbs", new HashMap<>());
    }

    @GET("/search")
    public ModelAndView<Map<String, Object>> investmentPage(@QueryParam String ticker, Context ctx) {
        if (!isLoggedIn(ctx)) {
            ctx.sendRedirect("/login");
            return null;
        }

        Instrument instrument = instrumentService.getInstrument(ticker);

        if (instrument == null) {
            return new ModelAndView<>("instrument-not-found.hbs", new HashMap<>());
        } else {
            Map<String, Object> map = new HashMap<>();

            try {
                map.put("price", priceService.getLatestPrice(instrument.getTicker()));
            } catch (Exception e) {
                map.put("price", "price not available");
            }

            if (instrument instanceof Equity equity) {
                map.put("equity", equity);
                return new ModelAndView<>("equities.hbs", map);
            } else if (instrument instanceof Etf etf) {
                map.put("etf", etf);
                return new ModelAndView<>("etfs.hbs", map);
            }

        }
        return new ModelAndView<>("instrument-not-found.hbs", new HashMap<>());
    }

    private boolean isLoggedIn(Context ctx) {
        if (ctx == null) return false;
        return !ctx.session().get("id").isMissing();
    }
}
