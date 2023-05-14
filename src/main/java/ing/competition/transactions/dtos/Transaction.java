package ing.competition.transactions.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private String debitAccount;
    private String creditAccount;
    private Float amount;
}
