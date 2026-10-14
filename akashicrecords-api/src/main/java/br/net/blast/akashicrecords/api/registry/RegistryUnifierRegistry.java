package br.net.blast.akashicrecords.api.registry;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Looks up the {@link RegistryUnifier} registered for a given Bukkit-side enum
 * type (e.g. {@code Material.class}, {@code EntityType.class}), mirroring
 * {@link br.net.blast.akashicrecords.api.event.EventBridgeRegistry} for registries
 * instead of events.
 */
public interface RegistryUnifierRegistry {

    <N, B> void register(@NotNull Class<B> bukkitType, @NotNull RegistryUnifier<N, B> unifier);

    @NotNull <N, B> Optional<RegistryUnifier<N, B>> lookup(@NotNull Class<B> bukkitType);
}
