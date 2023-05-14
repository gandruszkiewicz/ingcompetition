package ing.competition.transactions;

import ing.competition.transactions.dtos.Account;
import ing.competition.transactions.dtos.Transaction;
import ing.competition.transactions.service.TransactionService;
import io.quarkus.vertx.http.Compressed;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/transactions")
@Slf4j
public class TransactionsController {
    private final TransactionService transactionService;

    @Inject
    public TransactionsController(TransactionService transactionService) {
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
        log.info("Start report of {} transactions", transactionList.size());
        if(transactionList.isEmpty()){
            log.info("There are no transaction in the request collection. Return empty list");
            return RestResponse.ok(new ArrayList<>());
        }
        List<Account> accounts = this.transactionService.getAccounts(transactionList);
        long end = System.currentTimeMillis();
        log.info("Finish report. Processing takes {} milliseconds", (end - start));
        return RestResponse.ok(accounts);
    }
}
