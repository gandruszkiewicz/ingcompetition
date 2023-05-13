package ing.competition.onlinegame.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableGroup {
    private int availableSlots;
    private int orderIndex;
}
