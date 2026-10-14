package br.net.blast.umbriel.api.entity;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Wraps a platform's native entity (a mod-added mob included) as a Bukkit
 * {@link Entity}, mirroring {@link br.net.blast.umbriel.api.item.ItemBridge} for
 * living/non-living entities.
 *
 * @param <N> the platform's native entity type
 */
public interface EntityBridge<N> {

    /**
     * Wraps a native entity as its Bukkit counterpart. The same native entity must
     * always resolve to the same Bukkit instance for the lifetime of that entity.
     */
    @NotNull Entity toBukkit(@NotNull N nativeEntity);

    /**
     * Unwraps the native entity backing a Bukkit {@link Entity}.
     */
    @NotNull N toNative(@NotNull Entity bukkitEntity);

    /**
     * Whether the native entity's type was registered by a mod rather than vanilla
     * Minecraft, i.e. whether {@link br.net.blast.umbriel.api.registry.RegistryUnifier}
     * had to synthesize an {@link org.bukkit.entity.EntityType} for it.
     */
    boolean isModded(@NotNull N nativeEntity);
}
