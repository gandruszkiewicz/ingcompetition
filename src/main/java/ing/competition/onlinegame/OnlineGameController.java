package ing.competition.onlinegame;

import ing.competition.atmservice.dtos.ATM;
import ing.competition.atmservice.dtos.Task;
import ing.competition.atmservice.services.AtmService;
import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Order;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.service.OnlineGameService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/onlinegame")
public class OnlineGameController {
    @Inject
    OnlineGameService onlineGameService;
    @POST
    @Path("calculate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<List<Clan>> calculate(Players players) {
        return this.onlineGameService.calculateOrder(players);
    }
}
