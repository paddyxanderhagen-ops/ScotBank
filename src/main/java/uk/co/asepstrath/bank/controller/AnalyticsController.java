package uk.co.asepstrath.bank.controller;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
@Path("/profitLoss")
public class ProfitLossController {

    @GET("profitLossForStock")
    public String profitLossForStock (String ticker) {
        return "";
    }
}
