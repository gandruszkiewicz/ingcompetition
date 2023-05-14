package ing.competition.onlinegame.service;

import ing.competition.onlinegame.handlers.OrderHandler;
import ing.competition.onlinegame.dtos.Players;

public interface OnlineGameService {
    OrderHandler calculateOrder(Players players);

    Players generatePlayers(int groupCount, int numberOfClans, int maxPoints);
}
