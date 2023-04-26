package ing.competition.onlinegame.queue;

import java.util.*;

public class GameQueue <T> extends AbstractQueue<T> {

    private LinkedList<T> elements;
    public GameQueue(List<T> list){
        this.elements = new LinkedList<>(list);
    }
    public GameQueue(T first){
        this.elements = new LinkedList<>();
        this.elements.offer(first);
    }
    @Override
    public Iterator<T> iterator() {
        return this.elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean offer(T t) {
        if(t == null) return false;
        this.elements.add(t);
        return true;
    }

    /**
     * Inserts specified list of elements into this queue
     * @param list - list of elements to add
     * @return true if the element was added to this queue, else false
     */
    public boolean bulkOffer(List<T> list){
        if(list.isEmpty()) return false;
        this.elements.addAll(list);
        return true;
    }

    @Override
    public T poll() {
        Iterator<T> iter = elements.iterator();
        T t = null;
        if(iter.hasNext()) t = iter.next();
        if(t != null){
            iter.remove();
            return t;
        }
        return null;
    }

    @Override
    public T peek() {
        return this.elements.getFirst();
    }
}
