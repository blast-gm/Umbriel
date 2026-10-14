package br.net.blast.akashicrecords.api.registry;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Keeps a closed Bukkit enum-backed registry (such as {@code Material} or
 * {@code EntityType}) in sync with a platform's open, mod-extensible registry,
 * so entries a mod registers at load time become valid Bukkit constants instead
 * of causing plugins to see {@code null}/unrecognized lookups.
 * <p>
 * This generalizes the "synthesize a fake enum constant, then patch CraftBukkit's
 * lookup tables" trick applied to {@code Material}, {@code EntityType},
 * {@code World.Environment} and others. An implementation for a given Minecraft
 * version is responsible for the reflective/unsafe plumbing a specific
 * {@code <B>} enum needs; this interface only describes the contract plugins and
 * mods both rely on afterwards.
 *
 * @param <N> the platform's native registry entry type (e.g. {@code net.minecraft.world.item.Item})
 * @param <B> the Bukkit-side enum type being extended (e.g. {@code org.bukkit.Material})
 */
public interface RegistryUnifier<N, B> {

    /**
     * Ensures a Bukkit constant exists for the given native entry, synthesizing one
     * if the entry was registered by a mod and vanilla Bukkit has no constant for it.
     * Idempotent: calling this again for an already-unified entry is a no-op.
     */
    @NotNull B unify(@NotNull NamespacedKey key, @NotNull N nativeEntry);

    /**
     * The native entry a (possibly synthesized) Bukkit constant was unified from,
     * if any.
     */
    @NotNull Optional<N> nativeOf(@NotNull B bukkitConstant);

    /**
     * The Bukkit constant a native entry was unified to, if {@link #unify} has
     * already been called for it.
     */
    @NotNull Optional<B> bukkitOf(@NotNull N nativeEntry);
}
