package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.queue.GameQueue;

import java.util.List;
import java.util.Queue;

public interface OnlineGameService {
    GameQueue<GameQueue<Clan>> calculateOrder(Players players);
}
