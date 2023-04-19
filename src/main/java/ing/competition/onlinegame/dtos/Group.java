package ing.competition.onlinegame.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Group {
    private List<Clan> clans = new ArrayList<>();
    public Group(){

    }
    public Group(Clan clan){
        this.clans.add(clan);
    }
}
