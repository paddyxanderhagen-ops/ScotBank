package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import io.jooby.*;
import io.jooby.netty.NettyServer;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.controller.CashAccountController_;
import uk.co.asepstrath.bank.controller.HomeController_;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.CashAccountRepository;
import uk.co.asepstrath.bank.repository.InvestorRepository;
import uk.co.asepstrath.bank.service.APIService;
import uk.co.asepstrath.bank.service.InvestorService;
import javax.sql.DataSource;
import java.util.List;


public class App extends Jooby {

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule());

        setSessionStore(SessionStore.memory(Cookie.session("scotbank")));


        DataSource ds = require(DataSource.class);
        /*
        This will host any files in src/main/resources/assets on <host>/assets
        For example in the dice template (dice.hbs) it references "assets/dice.png" which is in resources/assets folder
         */
        assets("/assets/**", "/assets");
        assets("/service_worker.js","/service_worker.js");

        InvestorService investorService = new InvestorService();

        error(IllegalArgumentException.class, (ctx, cause, statusCode) -> {
            ctx.setResponseType("application/json");
            ctx.setResponseCode(StatusCode.BAD_REQUEST);
            Gson gson = new Gson();
            ctx.render(gson.toJson(cause.getMessage()));
        });

        mvc(new HomeController_(investorService));
        mvc(new CashAccountController_(investorService));
        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());


        get("/test", ctx -> {


            try (var conn = ds.getConnection();
                 var stmt = conn.createStatement();
                 var rs = stmt.executeQuery("SELECT 1")) {

                rs.next();
                return rs.getInt(1);
            }
        });

        get("/debug/investors", ctx -> {
            try (var conn = ds.getConnection();
                 var stmt = conn.createStatement();
                 var rs = stmt.executeQuery("SELECT * FROM investor")) {

                var list = new java.util.ArrayList<String>();

                while (rs.next()) {
                    list.add(rs.getString("id") + " - " + rs.getString("name") + " - " + rs.getDouble("cash_balance"));
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
    public void onStart() {
        Logger log = getLog();
        log.info("Starting Up...");



        DataSource ds = require(DataSource.class);

        try (var conn = ds.getConnection();
             var stmt = conn.createStatement()) {

            //stmt.execute("DROP TABLE IF EXISTS investor");

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS investor (
            id VARCHAR(36) PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            cash_balance DECIMAL(15,2) NOT NULL
            )""");



        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        APIService api = new APIService();
        InvestorRepository repo = new InvestorRepository(ds);

        try {
            List <Investor> investors = api.getAllInvestor().getInvestors();
            repo.saveAll(investors);
        } catch (Exception e) {
            log.warn("Failed to load investors from API", e);
        }
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

}
