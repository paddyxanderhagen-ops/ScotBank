package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.controller.CashAccountController;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.service.InvestorService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CashAccountControllerTests {

    class FakeInvestorService extends InvestorService{

        @Override
        public Investor getInvestor(String id){
            return new Investor("123","Johnny B Good",300);
        }


        public List<Investor> getAll(){
            return new ArrayList<>();
        }
    }

    @Test
    void depositRunsWithNoErrors(){
        InvestorService s = new FakeInvestorService();
        CashAccountController c = new CashAccountController(s);

        assertDoesNotThrow(() -> c.deposit(100.0,null));
    }

    @Test
    void withdrawRunsWithNoErrors(){
        InvestorService s = new FakeInvestorService();
        CashAccountController c = new CashAccountController(s);

        assertDoesNotThrow(() -> c.withdraw(100.0,null));
    }

}
