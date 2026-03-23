package uk.co.asepstrath.bank.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import uk.co.asepstrath.bank.model.Transaction;
import uk.co.asepstrath.bank.repository.TransactionRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class TransactionService {

    private final TransactionRepository repo;


    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }


    public List<Transaction> getTransactionsByInvestor(String id) {
        try {
            List<Transaction> transactions = repo.getTransactionsByInvestor(id);

            if (transactions == null) {
                throw new StatusCodeException(StatusCode.NOT_FOUND, "Transactions not found for investor");
            }
            return transactions;
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve transactions");
        }
    }

    public void createTransaction(String id, String type, double amount) {
        try {
            Stream<Transaction> tStream = repo.getAll().getTransactions().stream();
            List<Transaction> tList = tStream.sorted(Comparator.comparing(Transaction::getTimestamp).thenComparing(Transaction::getId).reversed()).toList();
            UUID oldTID = UUID.fromString(tList.getFirst().getId());
            System.out.println(oldTID);
            long newMSB = oldTID.getMostSignificantBits() + 1L;
            UUID newTID = new UUID(newMSB, UUID.randomUUID().getLeastSignificantBits());
            String dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Transaction t = new Transaction(newTID.toString(), dateTime, type.toUpperCase(), id, null, amount);
            repo.saveAll(List.of(t));
        } catch (SQLException e) {
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Failed to retrieve transactions");
        }
    }
}
