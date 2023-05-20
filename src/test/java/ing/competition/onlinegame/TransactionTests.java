package ing.competition.onlinegame;

import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Transaction;
import ing.competition.transactions.service.TransactionService;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@QuarkusTest
@Slf4j
public class TransactionTests {
    private final TransactionService transactionService;
    @Inject
    public TransactionTests(TransactionService transactionService){
        this.transactionService = transactionService;
    }
    @Test
    void testRequirements(){
        List<Transaction> transactionList = this.transactionService.generateTransactions();
        long start = System.currentTimeMillis();
        List<Account> accounts = this.transactionService.getAccounts(transactionList);
        long end = System.currentTimeMillis();
        log.info("Get accounts takes {} milliseconds", (end - start));

        // Check if there are any duplicates
        List<String> duplicates = this.getDuplicates(accounts);
        Assertions.assertTrue(duplicates.isEmpty());

        // Check is every account in list
        List<String> accountNumbers = Stream.concat(transactionList.stream().map(Transaction::getDebitAccount),
                transactionList.stream().map(Transaction::getCreditAccount)).toList();
        accountNumbers = removeDuplicates(accountNumbers);
        Assertions.assertEquals(accountNumbers.size(), accounts.size());

        // Check is ASC order
        boolean isTrue = true;
        for(int index = 0; index < accounts.size(); index++){
            if(index == 0) continue;
            float current = accounts.get(index).getBalance();
            float previous = accounts.get(index - 1).getBalance();
            if(current <= previous){
                isTrue = false;
                log.info("Order req failed at {}", index);
                break;
            }
        }
        Assertions.assertTrue(isTrue);
    }
    private List<String> getDuplicates(List<Account> accounts){
        List<String> duplicates = new ArrayList<>();
        Set<String> accountSet = new HashSet<>();
        for(Account account : accounts){
            String accountNumber = account.getAccountNumber();
            if(accountSet.contains(accountNumber)){
                duplicates.add(accountNumber);
            }else{
                accountSet.add(accountNumber);
            }
        }
        return duplicates;
    }
    private List<String> removeDuplicates(List<String> accountNumbers){
        Set<String> accountsSet = new HashSet<>(accountNumbers);
        return accountsSet.stream().toList();
    }
}
