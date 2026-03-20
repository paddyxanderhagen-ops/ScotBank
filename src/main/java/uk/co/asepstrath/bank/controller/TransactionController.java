package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.ModelAndView;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.QueryParam;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GET("/ViewAll")
    public ModelAndView<Map<String, Object>> getTransactionsForInvestor(@QueryParam String id, Context ctx) {
        List<Transaction> transactions = transactionService.getTransactionsByInvestor(id);
        Map<String, Object> model = new HashMap<>();
        model.put("transactions", transactions);
        model.put("investorId", id);
        return new ModelAndView("transactions.hbs", model);
    }

//    @GET("/Shares")
//    public HashMap<String, BigDecimal> investor(@QueryParam String id){
//        return (transactionService.getNumberOfShares(id));
//    }

//    @GET("/Shares/Value")
//    public BigDecimal value(@QueryParam String id){
//
//        return transactionService.getValueOfShares(investorId, transactionList);
//    }
}
