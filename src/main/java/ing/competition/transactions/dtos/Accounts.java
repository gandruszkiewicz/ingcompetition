package ing.competition.transactions.dtos;

import ing.competition.transactions.comparators.Comparators;

import java.util.Iterator;
import java.util.PriorityQueue;

public class Accounts extends PriorityQueue<Account> {
    public Accounts(int initCapacity){
        super(initCapacity, Comparators.sortByBalanceAsc());
    }
}
