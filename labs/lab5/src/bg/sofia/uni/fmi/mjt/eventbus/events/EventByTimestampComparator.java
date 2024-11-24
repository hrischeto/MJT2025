package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.util.Comparator;

public class EventByTimestampComparator<T extends Event<?>> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return o2.getTimestamp().compareTo(o1.getTimestamp());
    }
}