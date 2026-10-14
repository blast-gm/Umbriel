package io.izzel.arclight.common.mod.server.akashicrecords;

import br.net.blast.akashicrecords.api.registry.RegistryUnifier;
import io.izzel.arclight.common.bridge.bukkit.world.entity.EntityTypeBridge;
import io.izzel.arclight.common.mod.server.BukkitRegistry;
import net.minecraft.world.entity.EntityType;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Mirrors {@link UmbrielMaterialUnifier} for {@code org.bukkit.entity.EntityType},
 * backed by the enum constants {@link BukkitRegistry#registerAll} synthesizes for
 * every modded {@link EntityType} at boot.
 */
public final class UmbrielEntityTypeUnifier implements RegistryUnifier<EntityType<?>, org.bukkit.entity.EntityType> {

    @Override
    public @NotNull org.bukkit.entity.EntityType unify(@NotNull NamespacedKey key, @NotNull EntityType<?> nativeEntry) {
        var entityType = BukkitRegistry.getEntityType(nativeEntry);
        if (entityType == null) {
            throw new IllegalStateException("EntityType " + key + " was never unified by BukkitRegistry#loadEntities");
        }
        return entityType;
    }

    @Override
    public @NotNull Optional<EntityType<?>> nativeOf(org.bukkit.entity.@NotNull EntityType bukkitConstant) {
        return Optional.ofNullable(((EntityTypeBridge) (Object) bukkitConstant).bridge$getHandle());
    }

    @Override
    public @NotNull Optional<org.bukkit.entity.EntityType> bukkitOf(@NotNull EntityType<?> nativeEntry) {
        return Optional.ofNullable(BukkitRegistry.getEntityType(nativeEntry));
    }
}
