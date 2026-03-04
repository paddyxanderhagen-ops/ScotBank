package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.service.APIService;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTesting {

    @Test
    void MakeCashAccountFromInvestor_Empty(){
        Investor i = new Investor();
        CashAccountImplementation a = new CashAccountImplementation(i);
        assertNotNull(a);
    }

    @Test
    void MakeCashAccountFromInvestor_Populated(){
        Investor i = new Investor("JB123","Johnny Biggins", 40.0);
        CashAccountImplementation a = new CashAccountImplementation(i);
        assertNotNull(a);
    }

    @Test
    void InvestorDetailsCarryOver(){
        Investor i = new Investor("JB123","Johnny Biggins", 40.0);
        CashAccountImplementation a = new CashAccountImplementation(i);
        assertEquals(40.0,a.getBalance(),"Balance should carry over from investor.");
    }

    @Test
    void ChangesAffectInvestorClass(){
        Investor i = new Investor("JB123","Johnny Biggins", 40.0);
        CashAccountImplementation a = new CashAccountImplementation(i);
        a.deposit(60.0);
        assertEquals(100.0, i.getCashBalance(),"Deposits into the CashAccount should affect the underlying investor.");
    }

    @Test
    void getAllInvestorFromAPI () {
        APIService api = new APIService();
        InvestorService investorService = new InvestorService(api.getAllInvestor());

        assertEquals(api.getAllInvestor().getInvestors(), investorService.getAll());
    }

    @Test
    void getAllTransactionFromAPI () {
        APIService api = new APIService();
        TransactionService transactionService = new TransactionService(api.getAllTransaction());

        assertEquals(api.getAllTransaction().getTransactions(), transactionService.getAll());
    }







}