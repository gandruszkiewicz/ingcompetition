package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.queue.GameQueue;

public interface OnlineGameService {
    GameQueue<GameQueue<Clan>> calculateOrder(Players players);
    Players generatePlayers(int groupCount, int numberOfClans);
}
