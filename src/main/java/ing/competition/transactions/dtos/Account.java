package ing.competition.transactions.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    public Account(String account){
        this.account = account;
        this.balance = 0.00f;
    }
    private String account;
    private int debitCount;
    private int creditCount;
    private float balance;
    public void addTransaction(Transaction transaction){
        boolean isDebitAccount = this.compareAccounts(transaction.getDebitAccount());
        boolean isCreditAccount = this.compareAccounts(transaction.getCreditAccount());
        Float amount = transaction.getAmount();
        this.validateComparison(isDebitAccount, isCreditAccount, transaction);
        if(isDebitAccount){
            this.debitCount += 1;
            this.balance -= amount;
        }else {
            this.creditCount += 1;
            this.balance += amount;
        }
    }
    private boolean compareAccounts(String transactionAccount){
        return this.account.equals(transactionAccount);
    }
    private void validateComparison(boolean isDebitAccount, boolean isCreditAccount, Transaction transaction){
        String accountTransactionInfo = String.format(
                "Account number: %s, transaction: creditAccount - %s, debitAccount - %s",
                this.account, transaction.getCreditAccount(), transaction.getDebitAccount()
        );
        if(isCreditAccount && isDebitAccount){
            throw new IllegalArgumentException(
                    "Illegal transaction. Credit account and debit account are the same." +
                            accountTransactionInfo
            );
        }else if(!isCreditAccount && !isDebitAccount){
            throw new IllegalArgumentException(
                    "Illegal transaction. Transaction doesn't belong to that account. "+accountTransactionInfo
            );
        }
    }
}
