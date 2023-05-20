package ing.competition.transactions.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private String debitAccount;
    private String creditAccount;
    private Float amount;
    @Override
    public int hashCode(){
        return this.creditAccount.hashCode() + this.debitAccount.hashCode() + this.amount.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this.getClass() == obj.getClass()){
            Transaction temp = (Transaction) obj;
            return this.getCreditAccount().equals(temp.getCreditAccount())
                    && this.getDebitAccount().equals(temp.getDebitAccount())
                    && this.getAmount().equals(temp.getAmount());
        }
        return false;
    }
}
