package io.izzel.arclight.common.mod.server.akashicrecords;

import br.net.blast.akashicrecords.api.item.ItemBridge;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.v.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;

public final class UmbrielItemBridge implements ItemBridge<net.minecraft.world.item.ItemStack> {

    @Override
    public @NotNull org.bukkit.inventory.ItemStack toBukkit(net.minecraft.world.item.@NotNull ItemStack nativeStack) {
        return CraftItemStack.asCraftMirror(nativeStack);
    }

    @Override
    public net.minecraft.world.item.@NotNull ItemStack toNative(@NotNull org.bukkit.inventory.ItemStack bukkitStack) {
        return CraftItemStack.asNMSCopy(bukkitStack);
    }

    @Override
    public boolean isModded(net.minecraft.world.item.@NotNull ItemStack nativeStack) {
        if (nativeStack.isEmpty()) {
            return false;
        }
        var key = BuiltInRegistries.ITEM.getKey(nativeStack.getItem());
        return key != null && !key.getNamespace().equals("minecraft");
    }
}
