package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Order;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.service.OnlineGameService;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
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
    public List<Group> calculate(Players players) {
        long start = System.currentTimeMillis();
        Order result = this.onlineGameService.calculateOrder(players);
        long end = System.currentTimeMillis();
        log.debug("Finish calculation {}",(end - start));
        return result.getElements();
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
