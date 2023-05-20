package ing.competition.transactions.service;

import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TransactionTask implements Callable<List<Account>> {
    private final List<Transaction> transactions;
    private final ConcurrentHashMap<String,Account> concurrentHashMap;
    public TransactionTask(List<Transaction> transactions, ConcurrentHashMap<String, Account> concurrentHashMap) {
        this.transactions = transactions;
        this.concurrentHashMap = concurrentHashMap;
    }

    @Override
    public List<Account> call() {
        var iterator = transactions.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            this.processTransaction(this.concurrentHashMap, transaction, transaction.getCreditAccount());
            this.processTransaction(this.concurrentHashMap, transaction, transaction.getDebitAccount());
        }
        return new ArrayList<>();
    }

    private void processTransaction(ConcurrentHashMap<String,Account> accountHashMap,
                                    Transaction transaction, String accountNumber) {
        if (!accountHashMap.containsKey(accountNumber)) {
            this.addAccount(accountHashMap, transaction, accountNumber);
        } else {
            accountHashMap.get(accountNumber).addTransaction(transaction);
        }
    }

    private void addAccount(ConcurrentHashMap<String,Account> accountHashMap, Transaction transaction, String accountNumber) {
        Account account = new Account(accountNumber);
        account.addTransaction(transaction);
        accountHashMap.put(accountNumber, account);
    }
}
