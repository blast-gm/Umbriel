package br.net.blast.umbriel.api.platform;

import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Entry point used by plugins and library code to reach the current
 * {@link UmbrielPlatform}, without depending on which hybrid server loaded it.
 */
public final class Umbriel {

    private static final AtomicReference<UmbrielPlatform> PLATFORM = new AtomicReference<>();

    private Umbriel() {
    }

    /**
     * Discovers and caches the {@link UmbrielPlatform} published via
     * {@link ServiceLoader} by the running server, if one hasn't been resolved yet.
     *
     * @throws IllegalStateException if no platform implementation is on the classpath
     */
    public static @NotNull UmbrielPlatform platform() {
        var current = PLATFORM.get();
        if (current != null) {
            return current;
        }
        var loaded = ServiceLoader.load(UmbrielPlatform.class)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "No UmbrielPlatform implementation found on the classpath"));
        PLATFORM.compareAndSet(null, loaded);
        return PLATFORM.get();
    }
}
