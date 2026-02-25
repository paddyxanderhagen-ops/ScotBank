package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import io.jooby.*;
import io.jooby.netty.NettyServer;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.controller.CashAccountController_;
import uk.co.asepstrath.bank.controller.HomeController_;
import uk.co.asepstrath.bank.service.InvestorService;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class App extends Jooby {

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule("mem"));

        setSessionStore(SessionStore.memory(Cookie.session("scotbank")));

        /*
        This will host any files in src/main/resources/assets on <host>/assets
        For example in the dice template (dice.hbs) it references "assets/dice.png" which is in resources/assets folder
         */
        assets("/assets/**", "/assets");
        assets("/service_worker.js","/service_worker.js");

        DataSource ds = require(DataSource.class);
        Logger log = getLog();
        InvestorService investorService = new InvestorService();

        error(IllegalArgumentException.class, (ctx, cause, statusCode) -> {
            ctx.setResponseType("application/json");
            ctx.setResponseCode(StatusCode.BAD_REQUEST);
            Gson gson = new Gson();
            ctx.render(gson.toJson(cause.getMessage()));
        });

        mvc(new ExampleController_(ds,log));
        mvc(new HomeController_(investorService));
        mvc(new CashAccountController_(investorService));
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
    public void onStart() {
        Logger log = getLog();
        log.info("Starting Up...");

        // Fetch DB Source
        DataSource ds = require(DataSource.class);
        // Open Connection to DB
        try (Connection connection = ds.getConnection()) {
            //
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE `Example` (`Key` varchar(255),`Value` varchar(255))");
            stmt.executeUpdate("INSERT INTO Example " + "VALUES ('WelcomeMessage', 'Welcome to A Bank')");
        } catch (SQLException e) {
            log.error("Database Creation Error",e);
        }
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

}
