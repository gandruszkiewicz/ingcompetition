package ing.competition.transactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/transactions")
@Slf4j
public class TransactionsController {
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    @Inject
    public TransactionsController(TransactionService transactionService, ObjectMapper objectMapper) {
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @POST
    @Path("report")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Compressed
    @Blocking
    public RestResponse<String> report(String transactionPayload) throws JsonProcessingException {
        long start = System.currentTimeMillis();


        List<Transaction> transactionList = Arrays.stream(this.objectMapper.readValue(transactionPayload, Transaction[].class))
                .toList();
        long end = System.currentTimeMillis();
        log.info("Finish parsing {}", (end - start));
        start = System.currentTimeMillis();
        log.info("Start report of {} transactions", transactionList.size());
        if(transactionList.isEmpty()){
            log.info("There are no transaction in the request collection. Return empty list");
            return RestResponse.ok(this.objectMapper.writeValueAsString(new ArrayList<>()));
        }
        List<Account> accounts = new ArrayList<>();
       try{
           accounts = this.transactionService.getAccounts(transactionList);
       }catch (Exception ex){
           log.error("Error occurred during report generating {}", ex.getMessage());
           throw ex;
       }
        end = System.currentTimeMillis();
        log.info("Finish report. Processing takes {} milliseconds", (end - start));
        return RestResponse.ok(this.objectMapper.writeValueAsString(accounts));
    }
}
