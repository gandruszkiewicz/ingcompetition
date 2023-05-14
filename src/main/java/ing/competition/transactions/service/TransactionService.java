package ing.competition.transactions.service;

import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Transaction;

import java.util.List;
import java.util.stream.Stream;

public interface TransactionService {
    List<Account> getAccounts(List<Transaction> transactions);
    List<Transaction> generateTransactions();
}
