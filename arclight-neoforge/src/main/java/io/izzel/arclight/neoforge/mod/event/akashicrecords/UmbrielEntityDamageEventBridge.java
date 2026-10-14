package io.izzel.arclight.neoforge.mod.event.akashicrecords;

import br.net.blast.akashicrecords.api.event.EventBridge;
import br.net.blast.akashicrecords.api.platform.Akasha;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Bridges a pre-damage occurrence onto Bukkit's {@code EntityDamageEvent}. The
 * damage cause mapping is a best-effort translation of the native damage
 * source's message id, the same kind of lookup CraftBukkit's own damage
 * source handling performs.
 */
public final class UmbrielEntityDamageEventBridge
    implements EventBridge<LivingDamageEvent.Pre, EntityDamageEvent> {

    @Override
    public @NotNull EntityDamageEvent toBukkit(LivingDamageEvent.@NotNull Pre nativeEvent) {
        Entity entity = Akasha.platform().entityBridge().toBukkit(nativeEvent.getEntity());
        EntityDamageEvent.DamageCause cause = toDamageCause(nativeEvent.getSource());
        return new EntityDamageEvent(entity, cause, nativeEvent.getOriginalDamage());
    }

    @Override
    public void applyToNative(LivingDamageEvent.@NotNull Pre nativeEvent, @NotNull EntityDamageEvent bukkitEvent) {
        if (bukkitEvent.isCancelled()) {
            nativeEvent.setNewDamage(0.0F);
        } else {
            nativeEvent.setNewDamage((float) bukkitEvent.getFinalDamage());
        }
    }

    private static EntityDamageEvent.DamageCause toDamageCause(DamageSource source) {
        return switch (source.getMsgId()) {
            case "mob", "player", "sting" -> EntityDamageEvent.DamageCause.ENTITY_ATTACK;
            case "arrow", "thrown", "mob.ranged", "trident" -> EntityDamageEvent.DamageCause.PROJECTILE;
            case "fall" -> EntityDamageEvent.DamageCause.FALL;
            case "inFire" -> EntityDamageEvent.DamageCause.FIRE;
            case "onFire" -> EntityDamageEvent.DamageCause.FIRE_TICK;
            case "lava" -> EntityDamageEvent.DamageCause.LAVA;
            case "inWall" -> EntityDamageEvent.DamageCause.SUFFOCATION;
            case "drown" -> EntityDamageEvent.DamageCause.DROWNING;
            case "starve" -> EntityDamageEvent.DamageCause.STARVATION;
            case "cactus", "sweetBerryBush" -> EntityDamageEvent.DamageCause.CONTACT;
            case "outOfWorld" -> EntityDamageEvent.DamageCause.VOID;
            case "lightningBolt" -> EntityDamageEvent.DamageCause.LIGHTNING;
            case "magic", "indirectMagic" -> EntityDamageEvent.DamageCause.MAGIC;
            case "wither" -> EntityDamageEvent.DamageCause.WITHER;
            case "anvil", "fallingBlock", "fallingStalactite" -> EntityDamageEvent.DamageCause.FALLING_BLOCK;
            case "thorns" -> EntityDamageEvent.DamageCause.THORNS;
            case "explosion", "explosion.player" -> EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
            case "freeze" -> EntityDamageEvent.DamageCause.FREEZE;
            case "sonic_boom" -> EntityDamageEvent.DamageCause.SONIC_BOOM;
            default -> EntityDamageEvent.DamageCause.CUSTOM;
        };
    }
}
