package io.izzel.arclight.neoforge.mod.event.akashicrecords;

import br.net.blast.akashicrecords.api.event.EventBridge;
import br.net.blast.akashicrecords.api.platform.Akasha;
import net.minecraft.world.entity.EntitySpawnReason;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Bridges a mob's finalize-spawn occurrence onto Bukkit's
 * {@code CreatureSpawnEvent}, the same event CraftBukkit fires for vanilla
 * mob spawns, so a mod-added mob's spawn is equally observable to plugins.
 */
public final class UmbrielMobSpawnEventBridge
    implements EventBridge<FinalizeSpawnEvent, CreatureSpawnEvent> {

    @Override
    public @NotNull CreatureSpawnEvent toBukkit(@NotNull FinalizeSpawnEvent nativeEvent) {
        LivingEntity entity = (LivingEntity) Akasha.platform().entityBridge().toBukkit(nativeEvent.getEntity());
        return new CreatureSpawnEvent(entity, toSpawnReason(nativeEvent.getSpawnType()));
    }

    @Override
    public void applyToNative(@NotNull FinalizeSpawnEvent nativeEvent, @NotNull CreatureSpawnEvent bukkitEvent) {
        nativeEvent.setSpawnCancelled(bukkitEvent.isCancelled());
    }

    private static CreatureSpawnEvent.SpawnReason toSpawnReason(EntitySpawnReason spawnReason) {
        return switch (spawnReason) {
            case NATURAL -> CreatureSpawnEvent.SpawnReason.NATURAL;
            case CHUNK_GENERATION -> CreatureSpawnEvent.SpawnReason.CHUNK_GEN;
            case SPAWNER -> CreatureSpawnEvent.SpawnReason.SPAWNER;
            case TRIAL_SPAWNER -> CreatureSpawnEvent.SpawnReason.TRIAL_SPAWNER;
            case BREEDING -> CreatureSpawnEvent.SpawnReason.BREEDING;
            case MOB_SUMMONED -> CreatureSpawnEvent.SpawnReason.CUSTOM;
            case JOCKEY -> CreatureSpawnEvent.SpawnReason.JOCKEY;
            case REINFORCEMENT -> CreatureSpawnEvent.SpawnReason.REINFORCEMENTS;
            case BUCKET -> CreatureSpawnEvent.SpawnReason.BUCKET;
            case COMMAND -> CreatureSpawnEvent.SpawnReason.COMMAND;
            case DISPENSER -> CreatureSpawnEvent.SpawnReason.DISPENSE_EGG;
            case PATROL -> CreatureSpawnEvent.SpawnReason.PATROL;
            default -> CreatureSpawnEvent.SpawnReason.DEFAULT;
        };
    }
}
