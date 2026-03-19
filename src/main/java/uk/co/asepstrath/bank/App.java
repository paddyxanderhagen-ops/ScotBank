package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import io.jooby.*;
import io.jooby.exception.StatusCodeException;
import io.jooby.netty.NettyServer;
import uk.co.asepstrath.bank.controller.*;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.repository.TransactionRepository;
import uk.co.asepstrath.bank.service.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class App extends Jooby {

    private final APIService api;
    private InvestorRepository investorRepo;
    private InvestorService investorService;
    private TransactionRepository transactionRepo;
    private TransactionService transactionService;
    private AnalyticsService analyticsService;
    private PriceService priceService;
    private final DataSource ds;
    private Logger log;

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
        investorService = new InvestorService(investorRepo);
        priceService = new PriceService();
        transactionService = new TransactionService(transactionRepo);
        analyticsService = new AnalyticsService(transactionRepo, priceService, api);
        priceService = new PriceService();
        log = getLog();

        assets("/assets/**", "/assets");
        assets("/service_worker.js","/service_worker.js");

        error((ctx, cause, code) -> {
            ctx.setResponseCode(code);
            ctx.render("Error" + code.value() + cause.getMessage());
        });

        try {
            List <Investor> investors = api.getAllInvestor().getInvestors();

            investorRepo.saveAll(investors);
        } catch (Exception e) {
            log.warn("Failed to load investors from API", e);
        }

        try {
            List<Transaction> transactions = api.getAllTransaction().getTransactions();

            transactionRepo.saveAll(transactions);


            Set<String> tickers = new HashSet<>();
            for (Transaction t : transactions) {
                if (t.getTicker() != null && !t.getTicker().isBlank()) {
                    tickers.add(t.getTicker());
                }
            }

//            System.out.println("Preloading prices for " + tickers.size() + " tickers...");
//
//
//            tickers.parallelStream().forEach(ticker -> {
//                System.out.println("Caching: " + ticker);
//                api.getPricesForStock(ticker);
//                System.out.println("Done: " + ticker);
//            });
//
//            System.out.println("All prices cached!");

        } catch (Exception e) {
            log.warn("Failed to load transactions from API", e);
        }

        mvc(new HomeController_(investorService, ds, transactionService));
        mvc(new CashAccountController_(investorService, ds));
        mvc(new LoginController_());
        mvc(new TransactionController_(transactionService));
        mvc(new EtfController_());
        mvc(new EquitiesController_());
        //mvc(new PricesController());
        mvc(new AnalyticsController_(analyticsService));


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

        try {
            investorRepo.setUp();
            transactionRepo.setUp();
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Unable to access server.");
        }

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
