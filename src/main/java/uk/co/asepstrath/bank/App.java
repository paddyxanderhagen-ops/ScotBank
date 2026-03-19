package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import io.jooby.*;
import io.jooby.netty.NettyServer;
import uk.co.asepstrath.bank.controller.*;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.controller.CashAccountController_;
import uk.co.asepstrath.bank.controller.HomeController_;
import uk.co.asepstrath.bank.controller.LoginController_;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.PriceDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
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
    private TransactionDTO transactionDTO;
    private InvestorDTO investorDTO;
    private InvestorRepository investorRepo;
    private InvestorService investorService;
    private TransactionRepository transRepo;
    private TransactionService transactionService;
    private AnalyticsService analyticsService;
    private PriceService priceService;
    private final DataSource ds;
    private Logger log;
    private PriceDTO priceDTO;

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule());

        setSessionStore(SessionStore.memory(Cookie.session("scotbank")));

        ds = require(DataSource.class);
        investorRepo = new InvestorRepository(ds);
        transRepo = new TransactionRepository(ds);
        api = new APIService();
        investorService = new InvestorService(investorRepo);
        priceService = new PriceService();  // сначала priceService
        transactionService = new TransactionService(transRepo, priceService, api);  // потом transactionService
        analyticsService = new AnalyticsService(transRepo, priceService, api);
        log = getLog();

        /*
        This will host any files in src/main/resources/assets on <host>/assets
        For example in the dice template (dice.hbs) it references "assets/dice.png" which is in resources/assets folder
         */
        assets("/assets/**", "/assets");
        assets("/service_worker.js","/service_worker.js");

        error(IllegalArgumentException.class, (ctx, cause, statusCode) -> {
            ctx.setResponseType("application/json");
            ctx.setResponseCode(StatusCode.BAD_REQUEST);
            Gson gson = new Gson();
            ctx.render(gson.toJson(cause.getMessage()));
        });


        try {
            List <Investor> investors = api.getAllInvestor().getInvestors();

            investorRepo.saveAll(investors);
        } catch (Exception e) {
            log.warn("Failed to load investors from API", e);
        }

        try {
            transactionDTO = api.getAllTransaction();
            List<Transaction> transactions = transactionDTO.getTransactions();
            transRepo.saveAll(transactions);


            Set<String> tickers = new HashSet<>();
            for (Transaction t : transactions) {
                if (t.getTicker() != null && !t.getTicker().isBlank()) {
                    tickers.add(t.getTicker());
                }
            }

            System.out.println("Preloading prices for " + tickers.size() + " tickers...");


            tickers.parallelStream().forEach(ticker -> {
                System.out.println("Caching: " + ticker);
                api.getPricesForStock(ticker);
                System.out.println("Done: " + ticker);
            });

            System.out.println("All prices cached!");

        } catch (Exception e) {
            log.warn("Failed to load data from API", e);
        }


        mvc(new HomeController_(investorService, ds, transactionService));
        mvc(new CashAccountController_(investorService, ds));
        mvc(new LoginController_());
        mvc(new TransactionController_(transactionService));
        mvc(new EtfController_());
        mvc(new EquitiesController_());
        //mvc(new PricesController_());
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

        DataSource ds = require(DataSource.class);

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS investor (
            id VARCHAR(36) PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            cash_balance DECIMAL(15,2) NOT NULL
            )""");

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS transaction (
            id VARCHAR(36) PRIMARY KEY,
            timestamp VARCHAR(50) NOT NULL,
            type VARCHAR(50) NOT NULL,
            investor_id VARCHAR(36) NOT NULL,
            ticker VARCHAR(20),
            total_cash_amount DECIMAL(15,2) NOT NULL,
            FOREIGN KEY (investor_id) REFERENCES investor(id)
            )""");

        } catch (Exception e) {
            throw new RuntimeException(e);
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
