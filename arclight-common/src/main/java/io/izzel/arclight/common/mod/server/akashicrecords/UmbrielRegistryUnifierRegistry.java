package io.izzel.arclight.common.mod.server.akashicrecords;

import br.net.blast.akashicrecords.api.registry.RegistryUnifier;
import br.net.blast.akashicrecords.api.registry.RegistryUnifierRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class UmbrielRegistryUnifierRegistry implements RegistryUnifierRegistry {

    private final Map<Class<?>, RegistryUnifier<?, ?>> unifiers = new ConcurrentHashMap<>();

    @Override
    public <N, B> void register(@NotNull Class<B> bukkitType, @NotNull RegistryUnifier<N, B> unifier) {
        unifiers.put(bukkitType, unifier);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <N, B> Optional<RegistryUnifier<N, B>> lookup(@NotNull Class<B> bukkitType) {
        return Optional.ofNullable((RegistryUnifier<N, B>) unifiers.get(bukkitType));
    }
}
