package ing.competition.onlinegame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.handlers.OrderHandler;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.service.OnlineGameService;
import io.quarkus.vertx.http.Compressed;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Path("/onlinegame")
public class OnlineGameController {
    private final OnlineGameService onlineGameService;
    private final ObjectMapper objectMapper;

    @Inject
    public OnlineGameController(OnlineGameService onlineGameService, ObjectMapper objectMapper){
        this.onlineGameService = onlineGameService;
        this.objectMapper = objectMapper;
    }

    @POST
    @Path("calculate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Compressed
    @Blocking
    public RestResponse<String> calculate(String playersPayload) throws JsonProcessingException {
        Players players = this.objectMapper.readValue(playersPayload, Players.class);
        log.info("Start calculation for amount of {} players and group size {}",
                players.getClans().size(), players.getGroupCount());
        if(players.getClans().isEmpty()){
            log.info("There are no players in the request body. Return empty list");
            return RestResponse.ok(this.objectMapper.writeValueAsString(List.of()));
        }
        long start = System.currentTimeMillis();
        OrderHandler result = this.onlineGameService.calculateOrder(players);
        long end = System.currentTimeMillis();
        log.info("Finish calculation. Processing takes {} milliseconds",(end - start));
        return RestResponse.ok(this.objectMapper.writeValueAsString(result.getElements()));
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Players generate(){
        return this.onlineGameService.generatePlayers(1000,20000,1000);
    }
}
