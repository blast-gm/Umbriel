package io.izzel.arclight.common.mod.server.akashicrecords;

import br.net.blast.akashicrecords.api.entity.EntityBridge;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.v.entity.CraftEntity;
import org.jetbrains.annotations.NotNull;

public final class UmbrielEntityBridge implements EntityBridge<net.minecraft.world.entity.Entity> {

    @Override
    public @NotNull org.bukkit.entity.Entity toBukkit(net.minecraft.world.entity.@NotNull Entity nativeEntity) {
        return ((io.izzel.arclight.common.bridge.core.entity.EntityBridge) nativeEntity).bridge$getBukkitEntity();
    }

    @Override
    public net.minecraft.world.entity.@NotNull Entity toNative(org.bukkit.entity.@NotNull Entity bukkitEntity) {
        return ((CraftEntity) bukkitEntity).getHandle();
    }

    @Override
    public boolean isModded(net.minecraft.world.entity.@NotNull Entity nativeEntity) {
        var key = BuiltInRegistries.ENTITY_TYPE.getKey(nativeEntity.getType());
        return key != null && !key.getNamespace().equals("minecraft");
    }
}
