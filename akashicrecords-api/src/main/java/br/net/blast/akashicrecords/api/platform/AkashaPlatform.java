package br.net.blast.akashicrecords.api.platform;

import br.net.blast.akashicrecords.api.entity.EntityBridge;
import br.net.blast.akashicrecords.api.event.EventBridgeRegistry;
import br.net.blast.akashicrecords.api.item.ItemBridge;
import br.net.blast.akashicrecords.api.registry.RegistryUnifierRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * The integration point a hybrid mod+plugin server (Umbriel, Mohist, Cardboard,
 * Magma, or any future one) implements and exposes via
 * {@code META-INF/services/br.net.blast.akashicrecords.api.platform.AkashaPlatform} to let
 * Akasha drive its item/entity/event bridges.
 * <p>
 * A platform provides exactly one bridge implementation per concern; Akasha does
 * not merge or chain bridges from multiple platforms within a single server, since
 * only one native (mod-side) item/entity/event model exists per running server.
 */
public interface AkashaPlatform {

    /**
     * A short, stable identifier for this platform, e.g. {@code "arclight"}.
     */
    @NotNull String id();

    @NotNull ItemBridge<?> itemBridge();

    @NotNull EntityBridge<?> entityBridge();

    @NotNull EventBridgeRegistry eventBridges();

    @NotNull RegistryUnifierRegistry registryUnifiers();
}
