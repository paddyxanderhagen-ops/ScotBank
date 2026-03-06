package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.service.APIService;
import uk.co.asepstrath.bank.service.InvestorService;
import uk.co.asepstrath.bank.service.TransactionService;

import java.util.List;

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
    void constructInvestorServiceFromDTO () {
        APIService api = new APIService();
        InvestorDTO investorDTO = api.getAllInvestor();
        InvestorService investorService = new InvestorService(investorDTO);
        assertNotNull(investorService);
    }

    @Test
    void constructTransactionServiceFromDTO () {
        APIService api = new APIService();
        TransactionDTO transactionDTO = api.getAllTransaction();
        TransactionService transactionService = new TransactionService(transactionDTO);
        assertNotNull(transactionService);
    }

    @Test
    void getAllTransactions () {
        APIService api = new APIService();
        TransactionDTO transactionDTO = api.getAllTransaction();
        TransactionService transactionService = new TransactionService(transactionDTO);
        List<Transaction> transactions = transactionService.getAll();
        List<Transaction> expectedTransactionList = transactionDTO.getTransactions();
        assertEquals(expectedTransactionList, transactions);
    }

    @Test
    void getAllInvestors () {
        APIService api = new APIService();
        InvestorDTO investorDTO = api.getAllInvestor();
        InvestorService investorService = new InvestorService(investorDTO);
        List<Investor> investors = investorService.getAll();
        List<Investor> expectedInvestors = investorDTO.getInvestors();
        assertEquals(expectedInvestors, investors);
    }







}