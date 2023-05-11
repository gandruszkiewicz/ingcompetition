package ing.competition.onlinegame.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
public class Order extends AbstractQueue<Group> {
    private final LinkedList<Group> elements = new LinkedList<>();

    public Order(Group queue){
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
        if(clans== null) return false;
        this.elements.add(clans);
        return true;
    }

    @Override
    public Group poll() {
        Iterator<Group> iter = elements.iterator();
        Group t = null;
        if(iter.hasNext()) t = iter.next();
        if(t != null){
            iter.remove();
            return t;
        }
        return null;
    }

    @Override
    public Group peek() {
        return this.elements.getLast();
    }

    public void compareLastTwoAddedGroups(){
        Group last = this.elements.getLast();
        int nextToLastIndex = this.elements.size() - 2;
        int lastIndex = this.elements.size() - 1;
        if(nextToLastIndex < 0 || lastIndex == 0){
            return;
        }
        Group nextToLast = this.elements.get(nextToLastIndex);
        if(nextToLast.getNumberOfPlayers() < last.getNumberOfPlayers()){
            this.replaceGroups(lastIndex, last, nextToLastIndex, nextToLast);
        }
    }
    private void replaceGroups(int lastIndex, Group last, int nextToLastIndex, Group nextToLast){
        this.elements.set(lastIndex, nextToLast);
        this.elements.set(nextToLastIndex, last);
    }
}
