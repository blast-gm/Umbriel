package br.net.blast.akashicrecords.api.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Translates one occurrence of a mod-side (Forge/NeoForge/Fabric) event into the
 * equivalent Bukkit {@link Event}, dispatches it through Bukkit's plugin manager,
 * and carries the plugins' verdict (cancelled state, result, mutated fields) back
 * into the native event so the mod side observes the same outcome.
 * <p>
 * Implementations should not introduce a second, parallel event pipeline: the
 * native event stays authoritative and is only ever read from / written back to,
 * the same way a hybrid server typically funnels mod-side item interactions back through
 * CraftBukkit's existing {@code CraftEventFactory}.
 *
 * @param <N> the platform's native event type
 * @param <B> the corresponding {@link Event} subtype
 */
public interface EventBridge<N, B extends Event> {

    /**
     * Builds the Bukkit event that corresponds to this occurrence of the native event.
     * Returns {@code null} when the native event carries no state relevant to plugins
     * and dispatch should be skipped.
     */
    @Nullable B toBukkit(@NotNull N nativeEvent);

    /**
     * Applies the outcome of a dispatched Bukkit event (cancellation, result, any
     * plugin-mutated fields) back onto the native event that produced it.
     */
    void applyToNative(@NotNull N nativeEvent, @NotNull B bukkitEvent);
}
