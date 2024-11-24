package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.events.EventByPriorityAndTimestampComparator;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class EventBusImpl implements EventBus {

    private Map<Class<?>, Set<Subscriber<?>>> subscriptions;
    private Map<Class<?>, TreeSet<Event<?>>> events;

    public EventBusImpl() {
        subscriptions = new HashMap<>();
        events = new HashMap<>();
    }

    private <T extends Event<?>> void saveEvent(T event) {
        events.get(event.getClass()).add(event);
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {

        if (Objects.isNull(eventType)) {
            throw new IllegalArgumentException("EventType is null.");
        }

        if (Objects.isNull(subscriber)) {
            throw new IllegalArgumentException("Subscriber is null.");
        }

        if (!subscriptions.containsKey(eventType)) {
            subscriptions.put(eventType, new HashSet<>());
            events.put(eventType, new TreeSet<>(new EventByPriorityAndTimestampComparator<>()));
        }

        subscriptions.get(eventType).add(subscriber);

    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {

        if (Objects.isNull(eventType)) {
            throw new IllegalArgumentException("EventType is null.");
        }

        if (Objects.isNull(subscriber)) {
            throw new IllegalArgumentException("Subscriber is null.");
        }

        if (subscriptions.containsKey(eventType)) {
            if (!subscriptions.get(eventType).contains(subscriber)) {
                throw new MissingSubscriptionException("The given subscriber is not subscribed to the given eventType");
            }

            subscriptions.get(eventType).remove(subscriber);
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (Objects.isNull(event)) {
            throw new IllegalArgumentException("Event is null.");
        }

        if (!subscriptions.containsKey(event.getClass())) {
            subscriptions.put(event.getClass(), new HashSet<>());
            events.put(event.getClass(), new TreeSet<>(new EventByPriorityAndTimestampComparator<>()));
        }

        Set<Subscriber<?>> subscribers = subscriptions.get(event.getClass());

        if (Objects.isNull(subscribers) || subscribers.isEmpty()) {
            return;
        }

        for (Subscriber<?> genericSubscriber : subscribers) {
            Subscriber<? super T> sub = (Subscriber<? super T>) genericSubscriber;
            sub.onEvent(event);
        }

        saveEvent(event);
    }

    @Override
    public void clear() {
        subscriptions.clear();
        events.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from,
                                                       Instant to) {
        if (Objects.isNull(eventType)) {
            throw new IllegalArgumentException("EventType is null.");
        }

        if (Objects.isNull(from)) {
            throw new IllegalArgumentException("Timestamp from is null.");
        }

        if (Objects.isNull(to)) {
            throw new IllegalArgumentException("Timestamp to is null.");
        }

        if (from.equals(to) || !subscriptions.containsKey(eventType) || subscriptions.get(eventType).isEmpty()) {
            return Set.of();
        }

        TreeSet<Event<?>> eventLogs = events.get(eventType);
        Set<Event<?>> result = new TreeSet<>(new EventByPriorityAndTimestampComparator<>());

        for (Event<?> event : eventLogs) {
            if (event.getTimestamp().isAfter(from) && event.getTimestamp().isBefore(to)) {
                result.add(event);
            }
        }

        return Set.copyOf(result);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (Objects.isNull(eventType)) {
            throw new IllegalArgumentException("Event type is null.");
        }

        if (!subscriptions.containsKey(eventType) || subscriptions.get(eventType).isEmpty()) {
            return Set.of();
        }

        return Set.copyOf(subscriptions.get(eventType));
    }

}
