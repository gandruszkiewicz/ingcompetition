package ing.competition.transactions.service;

import ing.competition.transactions.TransactionTask;
import ing.competition.transactions.comparators.Comparators;
import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Transaction;
import ing.competition.transactions.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@ApplicationScoped
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    public List<Account> getAccounts(List<Transaction> transactions) {
        long start = System.currentTimeMillis();
        List<Account> accounts = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        var batches = StreamUtils.batches(transactions, 10000).toList();
        List<TransactionTask> transactionTasks = new ArrayList<>();
        for (var batch : batches) {
            transactionTasks.add(new TransactionTask(batch));
        }
        try {
            List<Future<List<Account>>> futures = executorService.invokeAll(transactionTasks);
            for (Future<List<Account>> future : futures) {
                accounts.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
        long end = System.currentTimeMillis();
        log.debug("Finish {}", (end - start));
        long startSort = System.currentTimeMillis();
        accounts.sort(Comparators.sortByBalanceAsc());
        long endSort = System.currentTimeMillis();
        log.debug("Finish sort {}", (endSort - startSort));
        return accounts;
    }

    public List<Transaction> generateTransactions() {
        int numberOfTransactions = 100000;
        List<String> accounts = this.generateAccounts();
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();
        float maxAmount = 1000000;
        for (int transactionIndex = 1; transactionIndex <= numberOfTransactions / 2; transactionIndex++) {
            int indexCredit = random.nextInt(accounts.size());
            String accountCredit = accounts.get(indexCredit);
            int indexDebit = indexCredit;
            while (indexDebit == indexCredit) {
                indexDebit = random.nextInt(accounts.size());
            }
            String accountDebit = accounts.get(indexDebit);
            float amount = random.nextFloat(maxAmount + 1) + 1;
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setCreditAccount(accountCredit);
            transaction.setDebitAccount(accountDebit);
            transactions.add(transaction);
        }
        return transactions;
    }

    public List<String> generateAccounts() {
        int numberOfAccounts = 1000;
        HashMap<String, ?> accounts = new HashMap<>();
        Random random = new Random();
        int max = 100000;
        int min = 1000000000;
        for (int account = 1; account <= numberOfAccounts; account++) {
            String accountNumber = String.valueOf(random.nextInt(max + min) + min);
            if (accounts.containsKey(accountNumber)) {
                accounts.put(accountNumber + accountNumber, null);
            } else {
                accounts.put(accountNumber, null);
            }
        }
        return accounts.keySet().stream().toList();
    }
}
