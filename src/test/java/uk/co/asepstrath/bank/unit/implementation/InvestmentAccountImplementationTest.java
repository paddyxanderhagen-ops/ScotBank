package uk.co.asepstrath.bank.unit.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.implementation.InvestmentAccountImplementation;
import uk.co.asepstrath.bank.model.Investor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvestmentAccountImplementationTest {

    private InvestmentAccountImplementation account;

    @BeforeEach
    void setUp() {
        account = new InvestmentAccountImplementation(new Investor());
    }

    @Test
    void buyStock_addsSharesCorrectly() {
        account.buyStock("PRMK", 10, 100.0);
        assertEquals(10, account.getShares("PRMK"));
    }

    @Test
    void buyStock_multiplePurchases_accumulateShares() {
        account.buyStock("GOOG", 10, 100.0);
        account.buyStock("GOOG", 5, 120.0);
        assertEquals(15, account.getShares("GOOG"));
    }

    @Test
    void buyStock_throwsIfAmountZeroOrNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> account.buyStock("KRIS", 0, 100));
        assertThrows(IllegalArgumentException.class,
                () -> account.buyStock("KRIS", -5, 100));
    }

    @Test
    void buyStock_throwsIfPriceInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> account.buyStock("PLUM", 10, 0));
        assertThrows(IllegalArgumentException.class,
                () -> account.buyStock("PLUM", 10, -10));
    }

    @Test
    void buyStock_throwsIfTickerInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> account.buyStock(null, 10, 100));
        assertThrows(IllegalArgumentException.class,
                () -> account.buyStock("", 10, 100));
    }

    @Test
    void buyStock_throwsIfMoreThanTwoDecimals() {
        assertThrows(IllegalArgumentException.class,
                () -> account.buyStock("AAPL", 10, 100.123));
    }

    @Test
    void sellStock_reducesSharesCorrectly() {
        account.buyStock("AAPL", 10, 100.0);
        account.sellStock("AAPL", 4, 110.0);
        assertEquals(6, account.getShares("AAPL"));
    }

    @Test
    void sellStock_throwsIfInsufficientShares() {
        account.buyStock("AAPL", 5, 100.0);
        assertThrows(IllegalArgumentException.class,
                () -> account.sellStock("AAPL", 10, 100.0));
    }

    @Test
    void sellStock_throwsIfInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> account.sellStock("AAPL", 0, 100));
        assertThrows(IllegalArgumentException.class,
                () -> account.sellStock("AAPL", 10, 0));
        assertThrows(IllegalArgumentException.class,
                () -> account.sellStock(null, 10, 100));
    }

    @Test
    void getShares_returnsZeroIfNoStock() {
        assertEquals(0, account.getShares("AAPL"));
    }

    @Test
    void getShares_handlesMultipleTickers() {
        account.buyStock("AAPL", 10, 100.0);
        account.buyStock("GOOG", 5, 200.0);
        assertEquals(10, account.getShares("AAPL"));
        assertEquals(5, account.getShares("GOOG"));
    }

    @Test
    void getBookPrice_calculatesAverageCorrectly() {
        account.buyStock("AAPL", 10, 100.0);
        account.buyStock("AAPL", 10, 200.0);
        double bookPrice = account.getBookPrice("AAPL");
        assertEquals(150.0, bookPrice);
    }

    @Test
    void getBookPrice_ignoresSellTransactions() {
        account.buyStock("AAPL", 10, 100.0);
        account.sellStock("AAPL", 5, 150.0);
        double bookPrice = account.getBookPrice("AAPL");
        assertEquals(100.0, bookPrice);
    }

    @Test
    void getBookPrice_returnsZeroIfNoShares() {
        assertEquals(0, account.getBookPrice("AAPL"));
    }

    @Test
    void sellAllShares_resultsInZeroShares() {
        account.buyStock("AAPL", 10, 100.0);
        account.sellStock("AAPL", 10, 120.0);
        assertEquals(0, account.getShares("AAPL"));
    }
}