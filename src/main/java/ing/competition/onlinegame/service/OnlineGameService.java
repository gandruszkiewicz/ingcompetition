package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.dtos.Group;

import java.util.Queue;

public interface OnlineGameService {
    Queue<Group> calculateOrder(Players players);
    Players generatePlayers(int groupCount, int numberOfClans);
}
