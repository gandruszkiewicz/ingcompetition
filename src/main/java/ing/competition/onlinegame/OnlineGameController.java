package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.handlers.OrderHandler;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.service.OnlineGameService;
import io.quarkus.vertx.http.Compressed;
import io.smallrye.common.annotation.Blocking;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@Path("/onlinegame")
public class OnlineGameController {
    private final OnlineGameService onlineGameService;

    @Inject
    public OnlineGameController(OnlineGameService onlineGameService){
        this.onlineGameService = onlineGameService;
    }

    @POST
    @Path("calculate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Compressed
    @Blocking
    public List<Group> calculate(Players players) {
        log.info("Start calculation for amount of {} players and group size {}",
                players.getClans().size(), players.getGroupCount());
        long start = System.currentTimeMillis();
        OrderHandler result = this.onlineGameService.calculateOrder(players);
        long end = System.currentTimeMillis();
        log.info("Finish calculation {}",(end - start));
        return result.getElements();
    }
}
