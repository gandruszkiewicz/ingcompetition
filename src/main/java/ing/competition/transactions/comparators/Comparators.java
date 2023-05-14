package ing.competition.transactions.comparators;

import ing.competition.transactions.dtos.Account;

import java.util.Comparator;

public class Comparators {
    private Comparators(){}
    public static Comparator<Account> sortByBalanceAsc() {
        return Comparator.comparingDouble(Account::getBalance);
    }
}
