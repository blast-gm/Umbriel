package br.net.blast.umbriel.api.platform;

import br.net.blast.umbriel.api.entity.EntityBridge;
import br.net.blast.umbriel.api.event.EventBridge;
import br.net.blast.umbriel.api.item.ItemBridge;
import org.jetbrains.annotations.NotNull;

/**
 * The integration point a hybrid mod+plugin server (Arclight, Mohist, Cardboard,
 * Magma, or any future one) implements and exposes via
 * {@code META-INF/services/br.net.blast.umbriel.api.platform.UmbrielPlatform} to let
 * Umbriel drive its item/entity/event bridges.
 * <p>
 * A platform provides exactly one bridge implementation per concern; Umbriel does
 * not merge or chain bridges from multiple platforms within a single server, since
 * only one native (mod-side) item/entity/event model exists per running server.
 */
public interface UmbrielPlatform {

    /**
     * A short, stable identifier for this platform, e.g. {@code "arclight"}.
     */
    @NotNull String id();

    @NotNull ItemBridge<?> itemBridge();

    @NotNull EntityBridge<?> entityBridge();

    @NotNull EventBridge<?, ?> eventBridge();
}
