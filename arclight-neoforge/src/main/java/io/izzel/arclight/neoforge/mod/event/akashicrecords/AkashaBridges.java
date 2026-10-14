package io.izzel.arclight.neoforge.mod.event.akashicrecords;

import br.net.blast.akashicrecords.api.entity.EntityBridge;
import br.net.blast.akashicrecords.api.item.ItemBridge;
import br.net.blast.akashicrecords.api.platform.Akasha;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Akasha#platform()} exposes its item/entity bridges as
 * {@code ItemBridge<?>}/{@code EntityBridge<?>}, which a caller that already
 * knows the platform's concrete native type (as this NeoForge-specific event
 * dispatch code does) cannot call directly: the captured wildcard rejects any
 * argument that isn't itself of that unnamed captured type. These helpers do
 * the one unchecked cast back to the known native types in a single place.
 */
final class AkashaBridges {

    private AkashaBridges() {
    }

    @SuppressWarnings("unchecked")
    static @NotNull org.bukkit.entity.Entity toBukkit(net.minecraft.world.entity.@NotNull Entity nativeEntity) {
        var bridge = (EntityBridge<net.minecraft.world.entity.Entity>) Akasha.platform().entityBridge();
        return bridge.toBukkit(nativeEntity);
    }

    @SuppressWarnings("unchecked")
    static @NotNull org.bukkit.inventory.ItemStack toBukkit(net.minecraft.world.item.@NotNull ItemStack nativeStack) {
        var bridge = (ItemBridge<net.minecraft.world.item.ItemStack>) Akasha.platform().itemBridge();
        return bridge.toBukkit(nativeStack);
    }
}
