package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Order;
import ing.competition.onlinegame.dtos.Players;

public interface OnlineGameService {
    Clan[][] calculateOrder(Players players);
}
