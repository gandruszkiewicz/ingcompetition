package ing.competition.onlinegame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.handlers.OrderHandler;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.service.OnlineGameService;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@Slf4j
@QuarkusTest
public class OnlineGameRequirementsTests {
    private final OnlineGameService onlineGameService;
    private final ObjectMapper objectMapper;

    @Inject
    public OnlineGameRequirementsTests(OnlineGameService onlineGameService, ObjectMapper objectMapper) {
        this.onlineGameService = onlineGameService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testGroups() throws JsonProcessingException {
        Players players = this.onlineGameService
                .generatePlayers(10, 100, 100);
        OrderHandler orderHandler = this.onlineGameService.calculateOrder(players);
        boolean hasOrderGroupEvalPos = true;
        for (int index = 0; index < orderHandler.size(); index++) {
            if (index == 0) continue;
            Group currentGroup = orderHandler.get(index);
            boolean currentGroupEval = OnlineGameTestUtils.evaluateGroup(currentGroup);
            if (!currentGroupEval) {
                this.wrongGroupEval(currentGroup);
                hasOrderGroupEvalPos = false;
                break;
            }
        }
        Assertions.assertTrue(hasOrderGroupEvalPos);
    }

    @Test
    public void testOrder() throws JsonProcessingException {

        Players players = this.onlineGameService.generatePlayers(
                1000, 20000, 1000
        );
        OrderHandler orderHandler = this.onlineGameService.calculateOrder(players);
        boolean testOrderPos = true;
        for (int index = 0; index < orderHandler.size(); index++) {
            if (index == 0) continue;
            Group beforeGroup = orderHandler.get(index - 1);
            Group currentGroup = orderHandler.get(index);

            //Check is before group stronger or have more players.
            boolean isBeforeGroupStronger = beforeGroup.getGroupFactor() > currentGroup.getGroupFactor();
            boolean isBeforeGroupMoreNumerousOrEq = beforeGroup.getNumberOfPlayers() >= currentGroup.getNumberOfPlayers();
            if (!(isBeforeGroupStronger || isBeforeGroupMoreNumerousOrEq)) {
                testOrderPos = false;
                this.wrongOrderEval(beforeGroup, currentGroup);
                break;
            }
        }
        Assertions.assertTrue(testOrderPos);
    }

    private void wrongOrderEval(Group beforeGroup, Group currentGroup) throws JsonProcessingException {
        String beforeGroupJson = this.objectMapper.writeValueAsString(beforeGroup.getClanList());
        String currentGroupJson = this.objectMapper.writeValueAsString(currentGroup.getClanList());
        log.debug("Comparing groups don't meet requirements: before group {} current group {}",
                beforeGroupJson, currentGroupJson
        );
    }

    private void wrongGroupEval(Group group) throws JsonProcessingException {
        String groupJson = this.objectMapper.writeValueAsString(group.getClanList());
        log.debug("Group has wrong order {}", groupJson);
    }

    private String getJsonPlayers() {
        return "{\n" +
                "    \"groupCount\": 4,\n" +
                "    \"clans\": [\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 76\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 71\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 69\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 69\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 65\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 62\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 60\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 47\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 2,\n" +
                "            \"points\": 89\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 43\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 2,\n" +
                "            \"points\": 76\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 1,\n" +
                "            \"points\": 37\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 93\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 92\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 2,\n" +
                "            \"points\": 61\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 87\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 86\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 82\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 76\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 60\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 2,\n" +
                "            \"points\": 39\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 53\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 45\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 40\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 36\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 38\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 2,\n" +
                "            \"points\": 12\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 14\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 12\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 3,\n" +
                "            \"points\": 7\n" +
                "        },\n" +
                "        {\n" +
                "            \"numberOfPlayers\": 2,\n" +
                "            \"points\": 5\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
