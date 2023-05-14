package ing.competition.transactions;

import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Accounts;
import ing.competition.transactions.dtos.Transaction;
import ing.competition.transactions.dtos.Transactions;
import ing.competition.transactions.service.TransactionService;
import io.quarkus.vertx.http.Compressed;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

@Path("/transactions")
@Slf4j
public class TransactionsController {
    private final TransactionService transactionService;

    @Inject
    public TransactionsController(TransactionService transactionService){
        this.transactionService = transactionService;
    }
    @POST
    @Path("report")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Compressed
    @Blocking
    public RestResponse<List<Account>> report(List<Transaction> transactionList) {
        long start = System.currentTimeMillis();
        log.debug("Start {}",start);
        List<Account> accounts = this.transactionService.getAccounts(transactionList);
        long end = System.currentTimeMillis();
        log.debug("Finish {}", (end - start));
        return RestResponse.ok(accounts);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<List<Transaction>> getTransactions(){
        return RestResponse.ok(this.transactionService.generateTransactions());
    }
}
