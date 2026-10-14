package io.izzel.arclight.neoforge.mod.event.akashicrecords;

import br.net.blast.akashicrecords.api.event.EventBridge;
import br.net.blast.akashicrecords.api.platform.Akasha;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * Subscribes the native events AkashicRecords ships {@link EventBridge}s for
 * to the NeoForge event bus and routes each occurrence through
 * {@link Akasha#platform()}'s event bridge registry, instead of dispatching
 * to Bukkit directly: the registry stays the single place that decides which
 * native event types plugins get to observe.
 */
public final class AkashicRecordsEventDispatcher {

    public static void register() {
        var bridges = Akasha.platform().eventBridges();
        bridges.register(PlayerInteractEvent.RightClickItem.class, new UmbrielItemUseEventBridge());
        bridges.register(PlayerInteractEvent.RightClickBlock.class, new UmbrielBlockInteractEventBridge());
        bridges.register(LivingDamageEvent.Pre.class, new UmbrielEntityDamageEventBridge());
        bridges.register(FinalizeSpawnEvent.class, new UmbrielMobSpawnEventBridge());
        NeoForge.EVENT_BUS.register(new AkashicRecordsEventDispatcher());
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        dispatch(PlayerInteractEvent.RightClickItem.class, event);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        dispatch(PlayerInteractEvent.RightClickBlock.class, event);
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onLivingDamagePre(LivingDamageEvent.Pre event) {
        dispatch(LivingDamageEvent.Pre.class, event);
    }

    @SubscribeEvent
    public void onFinalizeSpawn(FinalizeSpawnEvent event) {
        dispatch(FinalizeSpawnEvent.class, event);
    }

    private <N> void dispatch(Class<N> nativeType, N nativeEvent) {
        Akasha.platform().eventBridges().lookup(nativeType).ifPresent(bridge -> dispatchTyped(bridge, nativeEvent));
    }

    private <N, B extends Event> void dispatchTyped(EventBridge<N, B> bridge, N nativeEvent) {
        B bukkitEvent = bridge.toBukkit(nativeEvent);
        if (bukkitEvent != null) {
            Bukkit.getPluginManager().callEvent(bukkitEvent);
            bridge.applyToNative(nativeEvent, bukkitEvent);
        }
    }
}
