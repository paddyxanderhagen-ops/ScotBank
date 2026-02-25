package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.implementation.CashAccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAccount {

    @Test
    public void createAccount(){
        CashAccount a = new CashAccount();
        Assertions.assertTrue(a != null);
    }

    @Test
    void newAccount_ZeroBalance(){
        CashAccount a = new CashAccount();
        assertEquals(0.0,a.getBalance(),"New accounts should have no money.");
    }

    @Test
    void deposit_IncreasesBalance(){
        CashAccount a = new CashAccount();
        a.deposit(100.0);
        assertEquals(100.0,a.getBalance(),"Depositing 100 should increase balance from 0 to 100.");
    }

    @Test
    void withdraw_DecreasesBalance(){
        CashAccount a = new CashAccount();
        a.deposit(100.0);
        a.withdraw(75.0);
        assertEquals(25.0,a.getBalance(),"Withdrawing 75 with a balance of 100 leaves you with 25.");
    }

    @Test
    void deposit_MultipleTimes(){
        CashAccount a = new CashAccount();
        a.deposit(25.0);
        a.deposit(50.0);
        a.deposit(125.0);
        assertEquals(200.0,a.getBalance(),"Deposits of 25, 50 and 125 increase balance from 0 to 200.");
    }

    @Test
    void withdraw_MultipleTimes(){
        CashAccount a = new CashAccount();
        a.deposit(200.0);
        a.withdraw(50.0);
        a.withdraw(75.0);
        assertEquals(75.0,a.getBalance(),"Withdrawals of 50 and 75 decreases balance from 200 to 75.");
    }

    @Test
    void deposit_Zero(){
        CashAccount a = new CashAccount();
        a.deposit(75.0);
        a.deposit(0.0);
        assertEquals(75.0,a.getBalance(),"Depositing 0 should have no change on the account balance.");
    }

    @Test
    void withdraw_Zero(){
        CashAccount a = new CashAccount();
        a.deposit(75.0);
        a.withdraw(0.0);
        assertEquals(75.0,a.getBalance(),"Withdrawing 0 should have no change on the account balance.");
    }

    @Test
    void deposit_NegativeAmount(){
        CashAccount a = new CashAccount();
        assertThrows(IllegalArgumentException.class, () -> {a.deposit(-100.0);},"Depositing a negative amount should throw an error.");
    }

    @Test
    void withdraw_NegativeAmount(){
        CashAccount a = new CashAccount();
        assertThrows(IllegalArgumentException.class, () -> {a.withdraw(-100.0);},"Withdrawing a negative amount should throw an error.");
    }

    @Test
    void withdraw_MoreThanBalance(){
        CashAccount a = new CashAccount();
        a.deposit(75.0);
        assertThrows(IllegalArgumentException.class, () -> {a.withdraw(100);}, "Withdrawing more than account balance should throw IllegalArgumentException");
    }

    @Test
    void withdraw_MoreThan10K(){
        CashAccount a = new CashAccount();
        a.deposit(75000.0);
        assertThrows(IllegalArgumentException.class, () -> {a.withdraw(10001.0);}, "Withdrawing more than £10K is not permitted");
    }


}
