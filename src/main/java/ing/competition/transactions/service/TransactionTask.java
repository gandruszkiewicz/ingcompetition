package ing.competition.transactions.service;

import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class TransactionTask implements Callable<List<Account>> {
    private final List<Transaction> transactions;

    public TransactionTask(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public List<Account> call() {
        HashMap<String, Account> accountHashMap = new HashMap<>();
        Iterator<Transaction> iterator = transactions.iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            String creditAccount = transaction.getCreditAccount();
            String debitAccount = transaction.getDebitAccount();
            this.processTransaction(accountHashMap, transaction, creditAccount);
            this.processTransaction(accountHashMap, transaction, debitAccount);
        }
        return new ArrayList<>(accountHashMap.values());
    }

    private void processTransaction(HashMap<String, Account> accountHashMap, Transaction transaction, String accountNumber) {
        if (!accountHashMap.containsKey(accountNumber)) {
            this.addAccount(accountHashMap, transaction, accountNumber);
        } else {
            accountHashMap.get(accountNumber).addTransaction(transaction);
        }
    }

    private void addAccount(HashMap<String, Account> accountHashMap, Transaction transaction, String accountNumber) {
        Account account = new Account(accountNumber);
        account.addTransaction(transaction);
        accountHashMap.put(accountNumber, account);
    }
}
