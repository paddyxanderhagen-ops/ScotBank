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
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.service.APIService;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

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
         */
        assets("/assets/**", "/assets");
        assets("/service_worker.js","/service_worker.js");

        APIService api = new APIService();

        InvestorService investorService = new InvestorService(api.getAllInvestor());
        TransactionService transactionService = new TransactionService(api.getAllTransaction());
        investorService.getAll();
        transactionService.getAll();

        mvc(new HomeController_(investorService, transactionService));
        mvc(new CashAccountController_(investorService, transactionService));
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
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

}
