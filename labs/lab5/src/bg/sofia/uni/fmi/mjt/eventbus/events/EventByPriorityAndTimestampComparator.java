package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.util.Comparator;

public class EventByPriorityAndTimestampComparator<T extends Event<?>> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        EventByPriorityComparator<T> c1 = new EventByPriorityComparator<>();
        EventByTimestampComparator<T> c2 = new EventByTimestampComparator<>();

        if (c1.compare(o1, o2) == 0) {
            return c2.compare(o1, o2);
        } else {
            return c1.compare(o1, o2);
        }
    }
}