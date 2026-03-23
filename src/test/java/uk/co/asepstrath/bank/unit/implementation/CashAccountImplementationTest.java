package uk.co.asepstrath.bank.unit.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import uk.co.asepstrath.bank.implementation.CashAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.repository.InvestorRepository;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CashAccountImplementationTest {

    private CashAccountImplementation cashAccount;
    private Investor mockInvestor;
    private DataSource mockDataSource;
    private InvestorRepository mockRepo;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockDataSource = mock(DataSource.class);
        mockInvestor = mock(Investor.class);
        when(mockInvestor.getCashBalance()).thenReturn(100.00);
        mockRepo = mock(InvestorRepository.class);
        cashAccount = new CashAccountImplementation(mockInvestor, mockDataSource);
        when(mockDataSource.getConnection()).thenReturn(null);
    }

    @Test
    void testGetBalance() {
        assertEquals(100.00, cashAccount.getBalance());
    }

    @Test
    void testDepositNegativeAmountThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cashAccount.deposit(-50.00);
        });
        assertEquals("Deposit amount cannot be less than 0", exception.getMessage());
    }

    @Test
    void testDepositMoreThanTwoDecimalsThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cashAccount.deposit(50.123);
        });
        assertEquals("Cannot accept more than 2 decimal places", exception.getMessage());
    }


    @Test
    void testWithdrawMoreThanBalanceThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cashAccount.withdraw(150.00);
        });
        assertEquals("Withdraw amount cannot be greater than balance", exception.getMessage());
    }

    @Test
    void testWithdrawNegativeAmountThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cashAccount.withdraw(-50.00);
        });
        assertEquals("Withdraw amount cannot be less than 0", exception.getMessage());
    }
}