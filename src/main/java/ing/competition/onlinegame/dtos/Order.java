package ing.competition.onlinegame.dtos;

import ing.competition.onlinegame.comparators.Comparators;
import ing.competition.onlinegame.utils.GameQueueUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class Order extends AbstractQueue<Group> {
    private LinkedList<Group> elements = new LinkedList<>();
    private int maxPlayersGroup;
    private Queue<AvailableGroup> availableGroups = new LinkedList<>();

    public Order(Group queue, int maxPlayersGroup) {
        this.maxPlayersGroup = maxPlayersGroup;
        this.elements.add(queue);
    }

    @Override
    public Iterator<Group> iterator() {
        return this.elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean offer(Group clans) {
        this.compareLastTwoAddedGroups();
        if (clans == null) return false;
        this.elements.add(clans);
        return true;
    }

    @Override
    public Group poll() {
        Iterator<Group> iter = elements.iterator();
        Group t = null;
        if (iter.hasNext()) t = iter.next();
        if (t != null) {
            iter.remove();
            return t;
        }
        return null;
    }

    @Override
    public Group peek() {
        return this.elements.getLast();
    }

    public Group get(int index) {
        return this.elements.get(index);
    }

    private void checkAvailableGroups() {
        Group last = this.elements.getLast();
        if (this.availableGroups.size() > 0) {
            List<AvailableGroup> availableGroupsToDel = new ArrayList<>();
            for (AvailableGroup availableGroup : this.availableGroups) {
                List<Clan> clansToSwitch = last.getByNumerOfPlayers(availableGroup.getAvailableSlots());
                if (clansToSwitch.size() == 0) break;
                Group groupToOffer = this.elements.get(availableGroup.getOrderIndex());
                for (Clan clanToSwitch : clansToSwitch) {
                    groupToOffer.offer(clanToSwitch);
                }
                availableGroupsToDel.add(availableGroup);
            }
            this.availableGroups.removeAll(availableGroupsToDel);
        }
    }

    private void nextToLastFulfill() {
        Group last = this.elements.getLast();
        int lastIndex = this.elements.size() - 1;
        int nextToLastIndex = this.elements.size() - 2;
        Group nextToLast = this.elements.get(nextToLastIndex);
        int nextToLastAvailableSlots = GameQueueUtils.getGroupAvailableSlots(nextToLast, this.maxPlayersGroup);
        boolean isNextToLastFulfill = nextToLastAvailableSlots == 0;
        if (!isNextToLastFulfill) {
            List<Clan> clansToSwitch = last.getByNumerOfPlayers(nextToLastAvailableSlots);
            for (Clan clanToSwitch : clansToSwitch) {
                nextToLast.offer(clanToSwitch);
            }
            int lastAvailableSlot = GameQueueUtils.getGroupAvailableSlots(last, this.maxPlayersGroup);
            this.availableGroups.add(new AvailableGroup(lastAvailableSlot, lastIndex));
        }
    }

    public void compareLastTwoAddedGroups() {
        this.checkAvailableGroups();
        Group last = this.elements.getLast();
        int lastIndex = this.elements.size() - 1;
        int nextToLastIndex = this.elements.size() - 2;
        if (nextToLastIndex < 0 || lastIndex == 0) {
            return;
        }
        Group nextToLast = this.elements.get(nextToLastIndex);
        if (nextToLast.getNumberOfPlayers() < last.getNumberOfPlayers()) {
            this.replaceGroups(lastIndex, last, nextToLastIndex, nextToLast);
        }
        this.nextToLastFulfill();
        nextToLast = this.elements.get(this.elements.size() - 2);
        if (nextToLast.size() == 0) {
            this.elements.remove(nextToLast);
        }
    }

    public void sortByNumberOfPlayersDesc() {
        this.elements.sort(Comparators.sortGroupByNumberOfPlayersDesc());
        this.evaluateOrder();
    }

    private void replaceGroups(int lastIndex, Group last, int nextToLastIndex, Group nextToLast) {
        this.elements.set(lastIndex, nextToLast);
        this.elements.set(nextToLastIndex, last);
    }

    private void evaluateOrder() {
        for (int index = 1; index < this.elements.size(); index++) {
            Group currentGroup = this.elements.get(index);
            int nextToLastIndex = index - 1;
            Group previousGroup = this.elements.get(index - 1);
            boolean isPreviousStronger = previousGroup.getGroupFactor() > currentGroup.getGroupFactor();
            boolean isPreviousMoreNumerousOrEq = previousGroup.getNumberOfPlayers() >= currentGroup.getNumberOfPlayers();
            if (!(isPreviousMoreNumerousOrEq || isPreviousStronger)) {
                this.replaceGroups(index, currentGroup, nextToLastIndex, previousGroup);
            }
        }
    }
}
