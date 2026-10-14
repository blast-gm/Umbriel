package io.izzel.arclight.common.mod.server.akashicrecords;

import br.net.blast.akashicrecords.api.event.EventBridge;
import br.net.blast.akashicrecords.api.event.EventBridgeRegistry;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class UmbrielEventBridgeRegistry implements EventBridgeRegistry {

    private final Map<Class<?>, EventBridge<?, ? extends Event>> bridges = new ConcurrentHashMap<>();

    @Override
    public <N> void register(@NotNull Class<N> nativeType, @NotNull EventBridge<N, ? extends Event> bridge) {
        bridges.put(nativeType, bridge);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <N> Optional<EventBridge<N, ? extends Event>> lookup(@NotNull Class<N> nativeType) {
        return Optional.ofNullable((EventBridge<N, ? extends Event>) bridges.get(nativeType));
    }
}
