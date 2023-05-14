package ing.competition.atmservice;

import ing.competition.atmservice.dtos.ATM;
import ing.competition.atmservice.dtos.Task;
import ing.competition.atmservice.services.AtmService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/atms")
public class AtmServiceController {
    private final AtmService atmService;

    @Inject
    public AtmServiceController(AtmService atmService){
        this.atmService = atmService;
    }

    @POST
    @Path("calculateOrder")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ATM[] calculateOrder(Task[] order) {
        return this.atmService.calculateOrder(order);
    }
}
