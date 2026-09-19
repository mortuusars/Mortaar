package io.github.mortuusars.mortaar.resources;

import io.github.mortuusars.mortaar.Platform;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.function.Function;

public abstract class Resource {
    public static <T> Optional<Holder<T>> get(ResourceKey<T> key, HolderLookup.Provider registries) {
        return registries.lookupOrThrow(key.registryKey()).get(key)
              .map(holder -> holder); // Typical generics in java stuff
    }

    public static <T> Holder<T> getOrAny(ResourceKey<T> key, HolderLookup.Provider registries) {
        HolderLookup.RegistryLookup<T> registry = registries.lookupOrThrow(key.registryKey());
        return registry.get(key).orElse(registry.listElements().findFirst().orElseThrow());
    }

    public static <T> Holder<T> getOrThrow(ResourceKey<T> key, HolderLookup.Provider registries) {
        return registries.lookupOrThrow(key.registryKey()).getOrThrow(key);
    }

    public static <T, R> Optional<R> map(ResourceKey<T> key, Function<Holder<T>, R> mappingFunction, HolderLookup.Provider registries) {
        return get(key, registries).map(mappingFunction);
    }

    public static <T, R> R mapOrThrow(ResourceKey<T> key, Function<Holder<T>, R> mappingFunction, HolderLookup.Provider registries) {
        return mappingFunction.apply(getOrThrow(key, registries));
    }

    // --

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public static <T> Holder<T> getOrThrow(ResourceKey<T> key) {
        return getOrThrow(key, Platform.getCurrentServerOrThrow().registryAccess());
    }

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public static <T> Optional<Holder<T>> get(ResourceKey<T> key) {
        return get(key, Platform.getCurrentServerOrThrow().registryAccess());
    }

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public static <T, R> Optional<R> map(ResourceKey<T> key, Function<Holder<T>, R> mappingFunction) {
        return get(key).map(mappingFunction);
    }

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public static <T, R> R mapOrThrow(ResourceKey<T> key, Function<Holder<T>, R> mappingFunction) {
        return mappingFunction.apply(getOrThrow(key));
    }
}
