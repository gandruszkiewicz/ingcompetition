package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.queue.GameQueue;
import ing.competition.onlinegame.service.OnlineGameService;
import ing.competition.onlinegame.utils.ClanUtils;
import ing.competition.onlinegame.utils.GameQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/onlinegame")
public class OnlineGameController {
    @Inject
    OnlineGameService onlineGameService;
    @POST
    @Path("calculate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GameQueue<GameQueue<Clan>> calculate(Players players) {
        var result = this.onlineGameService.calculateOrder(players);
        var iterationsAvg = GameQueueUtils.iterList.stream().mapToInt(i -> i).sum() / GameQueueUtils.iterList.size();
        log.debug("Average number of iterations {}", iterationsAvg);
        return result;
    }
    @GET
    @Path("calculate/test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Players testCalculate(@RestQuery int groupCount, @RestQuery int numberOfClans) {

        return this.onlineGameService.generatePlayers(groupCount, numberOfClans);
    }

}
