package uk.co.asepstrath.bank.unit.controller;

import io.jooby.Context;
import io.jooby.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.CashAccountController;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;

class CashAccountControllerTest {

    private InvestorService is;
    private TransactionService ts;
    private DataSource ds;
    private Context ctx;
    private Session sess;
    private CashAccountController cac;

    @BeforeEach
    void setup() {
        is = mock(InvestorService.class);
        ts = mock(TransactionService.class);
        ds = mock(DataSource.class);
        ctx = mock(Context.class);
        sess = mock(Session.class);
        when(ctx.session()).thenReturn(sess);
    }

    @Test
    void depositWithNullContextDoesNothing() {
        cac = new CashAccountController(is, ds, ts);
        cac.deposit(100.0, null);
        verifyNoInteractions(is, ts);
    }

    @Test
    void withdrawWithNullContextDoesNothing() {
        cac = new CashAccountController(is, ds, ts);
        cac.withdraw(50.0, null);
        verifyNoInteractions(is, ts);
    }
}