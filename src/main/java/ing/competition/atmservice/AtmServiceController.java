package ing.competition.atmservice;

import ing.competition.atmservice.dtos.ATM;
import ing.competition.atmservice.dtos.RequestType;
import ing.competition.atmservice.dtos.Task;
import ing.competition.atmservice.services.AtmService;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Path("/atms")
public class AtmServiceController {
    @Inject
    AtmService atService;

    @POST
    @Path("calculateOrder")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ATM[] calculateOrder(Task[] order) {
        return this.atService.calculateOrder(order);
    }

    @GET
    public List<Task> getRandomList(@RestQuery int max) {
        int min = 1;
        List<Task> result = new ArrayList<>();
        List<String> myList = Arrays.asList(RequestType.FAILURE_RESTART.name(), RequestType.PRIORITY.name(),
                RequestType.SIGNAL_LOW.name(), RequestType.STANDARD.name());
        for (int i = 0; i < max; i++) {
            int region = ThreadLocalRandom.current().nextInt(min, max / 2);
            int atmId = ThreadLocalRandom.current().nextInt(min, max / 2);
            Random r = new Random();
            int randomitem = r.nextInt(myList.size());
            RequestType requestType = RequestType.valueOf(myList.get(randomitem));
            result.add(new Task(requestType, region, atmId));
        }
        return result;
    }
}
