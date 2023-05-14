package ing.competition.transactions.comparators;

import ing.competition.transactions.dtos.Account;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Account> sortByBalanceAsc() {
        return Comparator.comparingDouble(x -> {
            try {
                var balance = x.getBalance();
                return balance;
            } catch (NullPointerException ex) {
                throw ex;
            }
        });
    }
}
