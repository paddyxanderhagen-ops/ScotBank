package uk.co.asepstrath.bank;

import io.jooby.*;
import io.jooby.exception.StatusCodeException;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import io.jooby.netty.NettyServer;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.controller.*;
import uk.co.asepstrath.bank.model.Equity;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.*;
import uk.co.asepstrath.bank.service.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;


public class App extends Jooby {

    private final APIService api;
    private final DataSource ds;
    private final InvestorRepository investorRepo;
    private final InvestorService investorService;
    private final TransactionRepository transactionRepo;
    private final TransactionService transactionService;
    private final AnalyticsService analyticsService;
    private final PriceRepository priceRepo;
    private final PriceService priceService;
    private final InstrumentService instrumentService;
    private final InstrumentRepository instrumentRepo;
    private final ConstituentRepository constituentRepository;
    private final Logger log;

    public App() {
        this(true);
    }

    public App(boolean read) {
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule());

        setSessionStore(SessionStore.memory(Cookie.session("scotbank")));

        ds = require(DataSource.class);
        api = new APIService();

        investorRepo = new InvestorRepository(ds);
        transactionRepo = new TransactionRepository(ds);
        constituentRepository = new ConstituentRepository(ds);
        priceRepo = new PriceRepository(ds);
        instrumentRepo = new InstrumentRepository(ds);

        investorService = new InvestorService(investorRepo);
        priceService = new PriceService(priceRepo);
        transactionService = new TransactionService(transactionRepo);
        analyticsService = new AnalyticsService(transactionService, priceService, investorService);
        instrumentService = new InstrumentService(instrumentRepo);

        log = getLog();

        assets("/assets/**", "/assets");
        assets("/service_worker.js", "/service_worker.js");

        error((ctx, cause, code) -> {
            ctx.setResponseCode(code);
            if (ctx.header("Accept").value("").contains("application/json")) {
                ctx.setResponseType(MediaType.json);
                ctx.render("{\"error\": " + code.value() + ", \"message\": \"" + cause.getMessage() + "\"}");
            }
        });

        mvc(new HomeController_(investorService, transactionService));
        mvc(new CashAccountController_(investorService, ds, transactionService));
        mvc(new AnalyticsController_(analyticsService, api, priceRepo));
        mvc(new LoginController_(investorService));
        mvc(new TransactionController_(transactionService, investorService));
        mvc(new InstrumentController_(instrumentService, priceService));
        mvc(new PortfolioCategorisationController_(investorService, analyticsService, ds, instrumentService));
        mvc(new TaxReportController_(analyticsService));

        if (read) {
            try {
                investorRepo.setUp();
                transactionRepo.setUp();
                instrumentRepo.setUp();
                priceRepo.setUp();
                constituentRepository.setUp();
            } catch (SQLException e) {
                throw new StatusCodeException(StatusCode.SERVER_ERROR, "Unable to access server. " + e);
            }

            try {
                List<Investor> investors = api.getAllInvestor().getInvestors();

                investorRepo.saveAll(investors);
            } catch (SQLException e) {
                log.warn("Failed to load investors from API", e);
            }

            try {
                List<Transaction> transactions = api.getAllTransaction().getTransactions();
                transactionRepo.saveAll(transactions);

            } catch (SQLException e) {
                log.warn("Failed to load transaction from API", e);
            }

            try {
                List<Equity> equities = api.getAllEquities().getEquities();
                List<Etf> etfs = api.getAllEtf().getEtfs();

                String token = api.loadToken();

                instrumentRepo.saveAll(equities, etfs);

                constituentRepository.saveAll(etfs);


                for (Equity equity : equities) {
                    api.getAllPrices(equity.getTicker(), priceRepo, token);
                }

                for (Etf etf : etfs) {
                    api.getAllPrices(etf.getTicker(), priceRepo, token);
                }
            } catch (SQLException e) {
                log.warn("Failed to load assets and prices from API", e);
            }

            //view db in
            try {
                org.h2.tools.Server
                        .createWebServer("-web", "-webPort", "8082")
                        .start();
            } catch (SQLException e) {
                log.warn("Error:", e);
            }
        }


        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());

    }

    public static void main(final String[] args) {
        runApp(args, new NettyServer(new ServerOptions()), () -> new App());
    }

    /*
    This function will be called when the application starts up,
    it should be used to ensure that the DB is properly setup
     */
    public void onStart() {
        log.info("Starting Up...");
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

}
