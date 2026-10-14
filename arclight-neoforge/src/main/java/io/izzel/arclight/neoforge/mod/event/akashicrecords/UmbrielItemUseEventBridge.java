package io.izzel.arclight.neoforge.mod.event.akashicrecords;

import br.net.blast.akashicrecords.api.event.EventBridge;
import br.net.blast.akashicrecords.api.platform.Akasha;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * Bridges a right-click-with-item occurrence (no block/entity targeted) onto
 * Bukkit's {@code PlayerInteractEvent}, the same event CraftBukkit fires for
 * vanilla item use. Routing it through AkashicRecords instead lets a mod-fired
 * occurrence reach plugins identically.
 */
public final class UmbrielItemUseEventBridge
    implements EventBridge<PlayerInteractEvent.RightClickItem, org.bukkit.event.player.PlayerInteractEvent> {

    @Override
    public org.bukkit.event.player.@NotNull PlayerInteractEvent toBukkit(PlayerInteractEvent.@NotNull RightClickItem nativeEvent) {
        Player player = (Player) Akasha.platform().entityBridge().toBukkit(nativeEvent.getEntity());
        var item = Akasha.platform().itemBridge().toBukkit(nativeEvent.getItemStack());
        EquipmentSlot hand = nativeEvent.getHand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;
        return new org.bukkit.event.player.PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, item, null, null, hand);
    }

    @Override
    public void applyToNative(PlayerInteractEvent.@NotNull RightClickItem nativeEvent, org.bukkit.event.player.@NotNull PlayerInteractEvent bukkitEvent) {
        boolean denied = bukkitEvent.isCancelled() || bukkitEvent.useItemInHand() == Event.Result.DENY;
        nativeEvent.setCanceled(denied);
    }
}
