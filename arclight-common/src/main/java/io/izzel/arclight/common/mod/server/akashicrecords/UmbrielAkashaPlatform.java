package io.izzel.arclight.common.mod.server.akashicrecords;

import br.net.blast.akashicrecords.api.entity.EntityBridge;
import br.net.blast.akashicrecords.api.event.EventBridgeRegistry;
import br.net.blast.akashicrecords.api.item.ItemBridge;
import br.net.blast.akashicrecords.api.platform.AkashaPlatform;
import br.net.blast.akashicrecords.api.registry.RegistryUnifierRegistry;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * Umbriel's {@link AkashaPlatform}, published to plugins/mods via
 * {@code ServiceLoader}. Wires the concrete 1.21.11 bridges onto the underlying
 * CraftItemStack/CraftEntity/BukkitRegistry infrastructure instead of
 * introducing a second item/entity model.
 */
public final class UmbrielAkashaPlatform implements AkashaPlatform {

    public static final UmbrielAkashaPlatform INSTANCE = new UmbrielAkashaPlatform();

    private final ItemBridge<?> itemBridge = new UmbrielItemBridge();
    private final EntityBridge<?> entityBridge = new UmbrielEntityBridge();
    private final EventBridgeRegistry eventBridges = new UmbrielEventBridgeRegistry();
    private final RegistryUnifierRegistry registryUnifiers = new UmbrielRegistryUnifierRegistry();

    private UmbrielAkashaPlatform() {
        registryUnifiers.register(Material.class, new UmbrielMaterialUnifier());
        registryUnifiers.register(org.bukkit.entity.EntityType.class, new UmbrielEntityTypeUnifier());
    }

    /**
     * {@code ServiceLoader}'s Java 9+ provider-method convention, used here since
     * the constructor is private and {@link #INSTANCE} is the single instance.
     */
    public static UmbrielAkashaPlatform provider() {
        return INSTANCE;
    }

    @Override
    public @NotNull String id() {
        return "umbriel";
    }

    @Override
    public @NotNull ItemBridge<?> itemBridge() {
        return itemBridge;
    }

    @Override
    public @NotNull EntityBridge<?> entityBridge() {
        return entityBridge;
    }

    @Override
    public @NotNull EventBridgeRegistry eventBridges() {
        return eventBridges;
    }

    @Override
    public @NotNull RegistryUnifierRegistry registryUnifiers() {
        return registryUnifiers;
    }
}
