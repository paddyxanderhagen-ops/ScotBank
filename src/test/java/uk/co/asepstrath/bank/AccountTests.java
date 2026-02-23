package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.implementation.CashAccount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTests {

    private static CashAccount a = new CashAccount();

    @Test
    public void createAccount(){
        Assertions.assertTrue(a != null);
    }

    @Test
    void newAccount_ZeroBalance(){
        assertEquals(0,a.getBalance(),"New accounts should have no money.");
    }

    @Test
    void deposit_IncreasesBalance(){

        a.deposit(100);
        assertEquals(100,a.getBalance(),"Depositing 100 should increase balance from 0 to 100.");
    }

    @Test
    void withdraw_DecreasesBalance(){
        a.withdraw(75);
        assertEquals(25,a.getBalance(),"Withdrawing 75 with a balance of 100 leaves you with 25.");
    }

    @Test
    void deposit_MultipleTimes(){
        a.deposit(50);
        a.deposit(125);
        assertEquals(200,a.getBalance(),"Deposits of 50 and 125 increase balance from 25 to 200.");
    }

    @Test
    void withdraw_MultipleTimes(){
        a.withdraw(50);
        a.withdraw(75);
        assertEquals(75,a.getBalance(),"Withdrawals of 50 and 75 decreases balance from 200 to 75.");
    }

    @Test
    void deposit_Zero(){
        a.deposit(0);
        assertEquals(75,a.getBalance(),"Depositing 0 should have no change on the account balance.");
    }

    @Test
    void withdraw_Zero(){
        a.withdraw(0);
        assertEquals(75,a.getBalance(),"Withdrawing 0 should have no change on the account balance.");
    }

    @Test
    void deposit_NegativeAmount(){
        assertThrows(IllegalArgumentException.class, () -> {a.deposit(-100);},"Depositing a negative amount should throw an error.");
    }

    @Test
    void withdraw_NegativeAmount(){
        assertThrows(IllegalArgumentException.class, () -> {a.withdraw(-100);},"Withdrawing a negative amount should throw an error.");
    }


}
