package uk.co.asepstrath.bank.controller;

import io.jooby.Context;
import io.jooby.Session;
import io.jooby.value.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;

class CashAccountControllerTest {

    private CashAccountController cac;

    private InvestorService is;
    private DataSource ds;

    private Context ctx;
    private Session sess;

    @BeforeEach
    void setup() {
        is = mock(InvestorService.class);
        ds = mock(DataSource.class);
        cac = new CashAccountController(is, ds);
        ctx = mock(Context.class);
        sess = mock(Session.class);
        when(ctx.session()).thenReturn(sess);
    }
    /*
    @Test
    void depositCallsInvestorService() {
        double amount = 100.0;
        String id = "JD435";

        Investor investor = mock(Investor.class);

        // mock session value
        Value value = mock(Value.class);
        when(sess.get("id")).thenReturn(value);
        when(value.value()).thenReturn(id);

        when(is.getInvestor(id)).thenReturn(investor);
        cac.deposit(amount, ctx);
        verify(is.getInvestor(id));
    }
    */
    /*
    @Test
    void withdrawCallsInvestorService() {
        double amount = 5.0;
        String id = "JD123";

        Investor investor = mock(Investor.class);

        // mock session value
        Value value = mock(Value.class);
        when(sess.get("id")).thenReturn(value);
        when(value.value()).thenReturn(id);

        when(is.getInvestor(id)).thenReturn(investor);
        cac.withdraw(amount, ctx);
        verify(is).getInvestor(id);
    }
     */

    @Test
    void depositWithNullContextDoesNothing() {
        cac.deposit(100.0, null);

        verifyNoInteractions(is);
    }

    @Test
    void withdrawWithNullContextDoesNothing() {
        cac.withdraw(50.0, null);

        verifyNoInteractions(is);
    }
}