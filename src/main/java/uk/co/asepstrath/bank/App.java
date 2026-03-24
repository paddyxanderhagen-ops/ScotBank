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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class App extends Jooby {

    private final APIService api;
    private final DataSource ds;
    private final Authorisation authorisation;
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

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
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
        analyticsService = new AnalyticsService(transactionService, priceService);
        instrumentService = new InstrumentService(instrumentRepo);
        authorisation = new Authorisation(investorRepo);

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

        mvc(new HomeController_(investorService, ds, transactionService, authorisation));
        mvc(new CashAccountController_(investorService, ds, transactionService));
        mvc(new AnalyticsController_(analyticsService, authorisation, api, priceRepo));
        mvc(new LoginController_());
        mvc(new TransactionController_(transactionService, authorisation));
        mvc(new InstrumentController_(instrumentService, priceService));
        mvc(new PortfolioCategorisationController_(investorService, analyticsService, ds));
        mvc(new TaxReportController_(analyticsService));

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
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve transactions");
        }

        try {
            List<Transaction> transactions = api.getAllTransaction().getTransactions();

            transactionRepo.saveAll(transactions);

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


        } catch (Exception e) {
            log.warn("Issue loading assets" + e);
        }


        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());

    }

    public static void main(final String[] args) {
        runApp(args, new NettyServer(new ServerOptions()), App::new);
    }

    /*
    This function will be called when the application starts up,
    it should be used to ensure that the DB is properly setup
     */
    public void onStart() throws SQLException {
        Logger log = getLog();
        log.info("Starting Up...");

        //view db in browser
        org.h2.tools.Server
                .createWebServer("-web", "-webPort", "8082")
                .start();
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

}
