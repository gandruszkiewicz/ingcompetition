package ing.competition.transactions.service;

import ing.competition.exceptions.CustomRuntimeException;
import ing.competition.transactions.comparators.Comparators;
import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Transaction;
import ing.competition.transactions.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.*;

@ApplicationScoped
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final Random random = new Random();
    public List<Account> getAccounts(List<Transaction> transactions) {
        ConcurrentHashMap<String, Account> concurrentHashMap = new ConcurrentHashMap();
        ExecutorService executorService = Executors.newCachedThreadPool();
        var batches = StreamUtils.batches(transactions, 10000).toList();
        List<TransactionTask> transactionTasks = new ArrayList<>();
        for (var batch : batches) {
            transactionTasks.add(new TransactionTask(batch, concurrentHashMap));
        }
        try {
            List<Future<List<Account>>> futures = executorService.invokeAll(transactionTasks);
        } catch (InterruptedException e) {
            log.error("Error occurred TransactionServiceImpl.getAccounts {}",e.getMessage());
            Thread.currentThread().interrupt();
        }
        executorService.shutdown();
        ArrayList<Account> list = new ArrayList<>(concurrentHashMap.values());
        list.sort(Comparators.sortByBalanceAsc());
        return list;
    }

    public List<Transaction> generateTransactions() {
        int numberOfTransactions = 100000;
        List<String> accounts = this.generateAccounts();
        List<Transaction> transactions = new ArrayList<>();
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
