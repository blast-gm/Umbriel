package br.net.blast.akashicrecords.api.platform;

import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Entry point used by plugins and library code to reach the current
 * {@link AkashaPlatform}, without depending on which hybrid server loaded it.
 */
public final class Akasha {

    private static final AtomicReference<AkashaPlatform> PLATFORM = new AtomicReference<>();

    private Akasha() {
    }

    /**
     * Discovers and caches the {@link AkashaPlatform} published via
     * {@link ServiceLoader} by the running server, if one hasn't been resolved yet.
     *
     * @throws IllegalStateException if no platform implementation is on the classpath
     */
    public static @NotNull AkashaPlatform platform() {
        var current = PLATFORM.get();
        if (current != null) {
            return current;
        }
        var loaded = ServiceLoader.load(AkashaPlatform.class)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "No AkashaPlatform implementation found on the classpath"));
        PLATFORM.compareAndSet(null, loaded);
        return PLATFORM.get();
    }
}
