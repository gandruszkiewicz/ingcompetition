package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.service.OnlineGameService;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Queue;

@Slf4j
@Path("/onlinegame")
public class OnlineGameController {
    @Inject
    OnlineGameService onlineGameService;

    @POST
    @Path("calculate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Queue<Group> calculate(Players players) {
        return this.onlineGameService.calculateOrder(players);
    }

    @GET
    @Path("calculate/test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Players testCalculate(@RestQuery int groupCount,
                                 @RestQuery int numberOfClans,
                                 @RestQuery int maxPoints) {

        return this.onlineGameService.generatePlayers(
                groupCount,
                numberOfClans,
                maxPoints)
                ;
    }

}
