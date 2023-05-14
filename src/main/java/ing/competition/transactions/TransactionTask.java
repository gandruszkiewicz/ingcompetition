package ing.competition.transactions;

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
    private List<Transaction> transactions;
    public TransactionTask(List<Transaction> transactions){
        this.transactions = transactions;
    }
    @Override
    public List<Account> call() throws Exception {
        HashMap<String, Account> accountHashMap = new HashMap<>();
        Iterator<Transaction> iterator = transactions.iterator();
        long start = System.currentTimeMillis();
        log.debug("Processing transaction of thead start");
        while (iterator.hasNext()){
            Transaction transaction = iterator.next();
            String creditAccount = transaction.getCreditAccount();
            String debitAccount = transaction.getDebitAccount();
            this.processTransaction(accountHashMap, transaction, creditAccount);
            this.processTransaction(accountHashMap, transaction, debitAccount);
        }
        List<Account> accounts = new ArrayList<>(accountHashMap.values());
        long end = System.currentTimeMillis();
        log.debug("Processing transaction of thead finished {}", (end - start));
        return accounts;
    }
    private void processTransaction(HashMap<String, Account> accountHashMap, Transaction transaction, String accountNumber){
        if(!accountHashMap.containsKey(accountNumber)){
            this.addAccount(accountHashMap, transaction, accountNumber);
        }else{
            accountHashMap.get(accountNumber).addTransaction(transaction);
        }
    }
    private void addAccount(HashMap<String, Account> accountHashMap, Transaction transaction, String accountNumber){
        Account account = new Account(accountNumber);
        account.addTransaction(transaction);
        accountHashMap.put(accountNumber, account);
    }
}
