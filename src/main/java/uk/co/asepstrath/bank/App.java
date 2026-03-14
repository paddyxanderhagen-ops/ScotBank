package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import io.jooby.*;
import io.jooby.netty.NettyServer;
import uk.co.asepstrath.bank.controller.*;
//import uk.co.asepstrath.bank.example.ExampleController;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import io.jooby.netty.NettyServer;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.controller.CashAccountController_;
import uk.co.asepstrath.bank.controller.HomeController_;
import uk.co.asepstrath.bank.controller.LoginController_;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.repository.TransactionRepository;
import uk.co.asepstrath.bank.service.APIService;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class App extends Jooby {

    private final APIService api;
    private TransactionDTO transactionDTO;
    private InvestorRepository investorRepo;
    private InvestorService investorService;
    private TransactionRepository transRepo;
    private TransactionService transactionService;
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
        transRepo = new TransactionRepository(ds);
        investorService = new InvestorService(investorRepo);
        transactionService = new TransactionService(transRepo);
        log = getLog();

        DataSource ds = require(DataSource.class);
        /*
        This will host any files in src/main/resources/assets on <host>/assets
        For example in the dice template (dice.hbs) it references "assets/dice.png" which is in resources/assets folder
         */
        assets("/assets/**", "/assets");
        assets("/service_worker.js","/service_worker.js");

        api = new APIService();
        investorDTO = api.getAllInvestor();
        transactionDTO = api.getAllTransaction();

        //mvc(new ExampleController_(ds,log));
        //mvc(new InvestorController_());
        //mvc(new SignInController_());
        mvc(new TransactionController_());
        mvc(new EtfController_());
        mvc(new EquitiesController_());
        mvc(new PricesController_());
        InvestorService investorService = new InvestorService(investorDTO);
        TransactionService transactionService = new TransactionService(transactionDTO);

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
            List <Transaction> transactions = transactionDTO.getTransactions();

            transRepo.saveAll(transactions);
        } catch (Exception e) {
            log.warn("Failed to load transactions from API", e);
        }

        mvc(new HomeController_(investorService, ds, transactionService));
        mvc(new CashAccountController_(investorService, ds));
        mvc(new LoginController_());
        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());


        get("/test", ctx -> {


            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1")) {

                rs.next();
                return rs.getInt(1);
            }
        });

        get("/debug/investors", ctx -> {
            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM investor")) {

                List<String> list = new java.util.ArrayList<>();

                while (rs.next()) {
                    list.add(rs.getString("id") + " - " + rs.getString("name") + " - " + rs.getDouble("cash_balance"));
                }

                return list;
            }
        });

        get("/debug/transactions", ctx -> {
            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM transaction")) {

                List<String> list = new java.util.ArrayList<>();

                while (rs.next()) {
                    list.add(rs.getString("id") + " - " + rs.getString("timestamp")
                            + " - " + rs.getString("type") + rs.getString("investor_id")
                            + rs.getString("ticker") + rs.getDouble("total_cash_amount"));
                }
                return list;
            }
        });
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

        InvestorRepository investorRepo = new InvestorRepository(ds);
        TransactionRepository transRepo = new TransactionRepository(ds);

        try {
            List <Investor> investors = investorDTO.getInvestors();

            investorRepo.saveAll(investors);
        } catch (Exception e) {
            log.warn("Failed to load investors from API", e);
        }
        try {
            List <Transaction> transactions = transactionDTO.getTransactions();

            transRepo.saveAll(transactions);
        } catch (Exception e) {
            log.warn("Failed to load transactions from API", e);
        }
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

}
