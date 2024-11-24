package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.util.Comparator;

public class EventByPriorityComparator<T extends Event<?>> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return Integer.compare(o1.getPriority(), o2.getPriority());
    }
}
