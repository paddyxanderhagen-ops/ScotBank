package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountUnitTests {

    private static CashAccountImplementation a = new CashAccountImplementation();

    @Test
    public void createAccount(){
        CashAccountImplementation a = new CashAccountImplementation();
        Assertions.assertTrue(a != null);
    }

    @Test
    void newAccount_ZeroBalance(){
        CashAccountImplementation a = new CashAccountImplementation();
        assertEquals(0.0,a.getBalance(),"New accounts should have no money.");
    }

    @Test
    void deposit_IncreasesBalance(){
        CashAccountImplementation a = new CashAccountImplementation();
        a.deposit(100.0);
        assertEquals(100.0,a.getBalance(),"Depositing 100 should increase balance from 0 to 100.");
    }

    @Test
    void withdraw_DecreasesBalance(){
        CashAccountImplementation a = new CashAccountImplementation();
        a.deposit(100.0);
        a.withdraw(75.0);
        assertEquals(25.0,a.getBalance(),"Withdrawing 75 with a balance of 100 leaves you with 25.");
    }

    @Test
    void deposit_MultipleTimes(){
        CashAccountImplementation a = new CashAccountImplementation();
        a.deposit(25.0);
        a.deposit(50.0);
        a.deposit(125.0);
        assertEquals(200.0,a.getBalance(),"Deposits of 50 and 125 increase balance from 25 to 200.");
    }

    @Test
    void withdraw_MultipleTimes(){
        CashAccountImplementation a = new CashAccountImplementation();
        a.deposit(200.0);
        a.withdraw(50.0);
        a.withdraw(75.0);
        assertEquals(75.0,a.getBalance(),"Withdrawals of 50 and 75 decreases balance from 200 to 75.");
    }

    @Test
    void deposit_Zero(){
        CashAccountImplementation a = new CashAccountImplementation();
        a.deposit(75.0);
        a.deposit(0.0);
        assertEquals(75.0,a.getBalance(),"Depositing 0 should have no change on the account balance.");
    }

    @Test
    void withdraw_Zero(){
        CashAccountImplementation a = new CashAccountImplementation();
        a.deposit(75.0);
        a.withdraw(0.0);
        assertEquals(75.0,a.getBalance(),"Withdrawing 0 should have no change on the account balance.");
    }

    @Test
    void deposit_NegativeAmount(){
        CashAccountImplementation a = new CashAccountImplementation();
        assertThrows(IllegalArgumentException.class, () -> {a.deposit(-100);},"Depositing a negative amount should throw an error.");
    }

    @Test
    void withdraw_NegativeAmount(){
        CashAccountImplementation a = new CashAccountImplementation();
        assertThrows(IllegalArgumentException.class, () -> {a.withdraw(-100);},"Withdrawing a negative amount should throw an error.");
    }


}