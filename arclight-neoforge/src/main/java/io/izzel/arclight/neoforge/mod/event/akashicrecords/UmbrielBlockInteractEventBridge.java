package io.izzel.arclight.neoforge.mod.event.akashicrecords;

import br.net.blast.akashicrecords.api.event.EventBridge;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.bukkit.craftbukkit.v.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * Bridges a right-click-on-block occurrence onto Bukkit's
 * {@code PlayerInteractEvent}, mirroring the block/face CraftBukkit itself
 * reports for the same interaction.
 */
public final class UmbrielBlockInteractEventBridge
    implements EventBridge<PlayerInteractEvent.RightClickBlock, org.bukkit.event.player.PlayerInteractEvent> {

    @Override
    public org.bukkit.event.player.@NotNull PlayerInteractEvent toBukkit(PlayerInteractEvent.@NotNull RightClickBlock nativeEvent) {
        Player player = (Player) AkashaBridges.toBukkit(nativeEvent.getEntity());
        var item = AkashaBridges.toBukkit(nativeEvent.getItemStack());
        var block = CraftBlock.at(nativeEvent.getLevel(), nativeEvent.getPos());
        var face = CraftBlock.notchToBlockFace(nativeEvent.getFace());
        EquipmentSlot hand = nativeEvent.getHand() == InteractionHand.OFF_HAND ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;
        return new org.bukkit.event.player.PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, item, block, face, hand);
    }

    @Override
    public void applyToNative(PlayerInteractEvent.@NotNull RightClickBlock nativeEvent, org.bukkit.event.player.@NotNull PlayerInteractEvent bukkitEvent) {
        boolean denied = bukkitEvent.isCancelled() || bukkitEvent.useInteractedBlock() == Event.Result.DENY;
        nativeEvent.setCanceled(denied);
        nativeEvent.setUseBlock(toTriState(bukkitEvent.useInteractedBlock()));
        nativeEvent.setUseItem(toTriState(bukkitEvent.useItemInHand()));
    }

    private static TriState toTriState(Event.Result result) {
        return switch (result) {
            case ALLOW -> TriState.TRUE;
            case DENY -> TriState.FALSE;
            case DEFAULT -> TriState.DEFAULT;
        };
    }
}
