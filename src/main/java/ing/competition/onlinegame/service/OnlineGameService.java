package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Order;
import ing.competition.onlinegame.dtos.Players;

import java.util.List;

public interface OnlineGameService {
    List<List<Clan>> calculateOrder(Players players);
}
