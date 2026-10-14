package io.izzel.arclight.common.mod.server.akashicrecords;

import br.net.blast.akashicrecords.api.registry.RegistryUnifier;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Looks up the {@link Material} unified for a native {@link Item} by
 * {@link io.izzel.arclight.common.mod.server.BukkitRegistry#loadMaterials()}, which
 * already runs at server boot before any plugin or this platform's consumers can
 * observe a registry entry, so {@link #unify} never actually needs to synthesize
 * anything itself, it only asserts the invariant holds.
 */
public final class UmbrielMaterialUnifier implements RegistryUnifier<Item, Material> {

    @Override
    public @NotNull Material unify(@NotNull NamespacedKey key, @NotNull Item nativeEntry) {
        var material = CraftMagicNumbers.getMaterial(nativeEntry);
        if (material == null) {
            throw new IllegalStateException("Item " + key + " was never unified by BukkitRegistry#loadMaterials");
        }
        return material;
    }

    @Override
    public @NotNull Optional<Item> nativeOf(@NotNull Material bukkitConstant) {
        return Optional.ofNullable(CraftMagicNumbers.getItem(bukkitConstant));
    }

    @Override
    public @NotNull Optional<Material> bukkitOf(@NotNull Item nativeEntry) {
        return Optional.ofNullable(CraftMagicNumbers.getMaterial(nativeEntry));
    }
}
