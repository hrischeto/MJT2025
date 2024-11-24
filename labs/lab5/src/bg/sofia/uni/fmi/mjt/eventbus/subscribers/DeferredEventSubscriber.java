package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.events.EventByPriorityAndTimestampComparator;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private Set<T> events;

    public DeferredEventSubscriber() {
        events = new TreeSet<>(new EventByPriorityAndTimestampComparator<T>());
    }

    @Override
    public void onEvent(T event) {
        if (Objects.isNull(event))
            throw new IllegalArgumentException("Event to add is null.");

        events.add(event);
    }

    @Override
    public Iterator<T> iterator() {
        return events.iterator();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

}