package ing.competition.onlinegame.queue;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class GameQueue <T> extends AbstractQueue<T> {

    private LinkedList<T> elements;
    public GameQueue(T[] array){
        this.elements = new LinkedList<>(Arrays.asList(array));
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

    @Override
    public T poll() {
        Iterator<T> iter = elements.iterator();
        T t = iter.next();
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
