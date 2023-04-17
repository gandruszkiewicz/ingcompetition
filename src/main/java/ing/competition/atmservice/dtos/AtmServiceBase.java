package ing.competition.atmservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
abstract class AtmServiceBase {
    public int region;
    public int atmId;
}
