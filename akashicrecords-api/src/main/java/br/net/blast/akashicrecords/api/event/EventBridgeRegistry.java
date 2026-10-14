package br.net.blast.akashicrecords.api.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Looks up the {@link EventBridge} registered for a given native event type. A
 * platform registers one bridge per native event class it knows how to translate;
 * native event types nobody registered a bridge for are simply not dispatched to
 * plugins.
 */
public interface EventBridgeRegistry {

    <N> void register(@NotNull Class<N> nativeType, @NotNull EventBridge<N, ? extends Event> bridge);

    @NotNull <N> Optional<EventBridge<N, ? extends Event>> lookup(@NotNull Class<N> nativeType);
}
