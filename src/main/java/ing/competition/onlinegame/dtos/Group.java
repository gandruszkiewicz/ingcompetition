package ing.competition.onlinegame.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Group {
    private List<Clan> clans;
    public Group(){
        this.setClans(new ArrayList<>());
    }
    public Group(Clan clan){
        this.setClans(List.of(clan));
    }
}
