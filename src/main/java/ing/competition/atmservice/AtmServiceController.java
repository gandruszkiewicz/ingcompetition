package ing.competition.atmservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ing.competition.atmservice.dtos.ATM;
import ing.competition.atmservice.dtos.Task;
import ing.competition.atmservice.services.AtmService;
import io.quarkus.vertx.http.Compressed;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/atms")
public class AtmServiceController {
    private final AtmService atmService;
    private final ObjectMapper objectMapper;

    @Inject
    public AtmServiceController(AtmService atmService, ObjectMapper objectMapper){
        this.atmService = atmService;
        this.objectMapper = objectMapper;
    }

    @POST
    @Path("calculateOrder")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    @Compressed
    public RestResponse<String> calculateOrder(String orderPayload) throws JsonProcessingException {
        long start = System.currentTimeMillis();
        log.info("Start ATMS/calculateOrder");
        final Task[] order = this.objectMapper.readValue(orderPayload, Task[].class);
        final ATM[] atms = this.atmService.calculateOrder(order);
        long end = System.currentTimeMillis();
        log.info("Finish ATMS/calculateOrder.... takes {} milliseconds", (end - start));
        return RestResponse.ok(this.objectMapper.writeValueAsString(atms));
    }
}
