package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Order;
import ing.competition.onlinegame.dtos.Players;

import java.util.List;

public interface OnlineGameService {
    Order calculateOrder(Players players);

    Players generatePlayers(int groupCount, int numberOfClans, int maxPoints);
}
