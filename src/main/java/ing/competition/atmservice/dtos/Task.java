package ing.competition.atmservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private RequestType requestType;
    private int region;
    private int atmId;
}
