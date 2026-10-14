package br.net.blast.akashicrecords.api.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Converts a platform's native item representation (a Forge/NeoForge/Fabric
 * {@code net.minecraft.world.item.ItemStack}, for example) to and from
 * {@link ItemStack org.bukkit.inventory.ItemStack}.
 * <p>
 * A bridge treats the native stack as the single source of truth: {@link #toBukkit}
 * must not copy state into a second, independently-mutable model. Implementations
 * typically wrap the native stack and delegate reads/writes back into it, the same
 * way a CraftItemStack can ride on the live NMS ItemStack rather than cloning it.
 *
 * @param <N> the platform's native item stack type
 */
public interface ItemBridge<N> {

    /**
     * Wraps a native item stack as a Bukkit {@link ItemStack}.
     */
    @NotNull ItemStack toBukkit(@NotNull N nativeStack);

    /**
     * Unwraps (or constructs) the native item stack backing a Bukkit {@link ItemStack}.
     */
    @NotNull N toNative(@NotNull ItemStack bukkitStack);

    /**
     * Whether the native stack's item type was registered by a mod rather than vanilla
     * Minecraft, i.e. whether {@link br.net.blast.akashicrecords.api.registry.RegistryUnifier}
     * had to synthesize a {@link org.bukkit.Material} for it.
     */
    boolean isModded(@NotNull N nativeStack);
}
