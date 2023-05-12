package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Order;
import ing.competition.onlinegame.dtos.Players;

public interface OnlineGameService {
    Order calculateOrder(Players players);
    Players generatePlayers(int groupCount, int numberOfClans, int maxPoints);
}
