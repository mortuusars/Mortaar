package io.github.mortuusars.mortaar.resources;

import io.github.mortuusars.mortaar.Platform;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ResourceDefinition<T, O> {
    private final ResourceKey<T> key;
    private final Function<Holder<T>, O> objectConstructor;

    protected ResourceDefinition(ResourceKey<T> key, Function<Holder<T>, O> objectConstructor) {
        this.key = key;
        this.objectConstructor = objectConstructor;
    }

    public static <T, O> ResourceDefinition<T, O> create(ResourceKey<? extends Registry<T>> registryKey,
                                                         ResourceLocation location, Function<Holder<T>, O> objectConstructor) {
        return new ResourceDefinition<>(ResourceKey.create(registryKey, location), objectConstructor);
    }

    public static <T> ResourceDefinition<T, T> create(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation location) {
        return create(registryKey, location, Holder::value);
    }

    // --

    public Holder.Reference<T> getHolderOrThrow(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(key.registryKey()).getOrThrow(key);
    }

    public Optional<Holder.Reference<T>> getHolder(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(key.registryKey()).get(key);
    }

    public O getOrThrow(HolderLookup.Provider registries) {
        return objectConstructor.apply(getHolderOrThrow(registries));
    }

    public Optional<O> get(HolderLookup.Provider registries) {
        return getHolder(registries).map(objectConstructor);
    }

    // --

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public Holder.Reference<T> getHolderOrThrow() {
        return getHolderOrThrow(Platform.getCurrentServerOrThrow().registryAccess());
    }

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public Optional<Holder.Reference<T>> getHolder() {
        return getHolder(Platform.getCurrentServerOrThrow().registryAccess());
    }

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public O getOrThrow() {
        return getOrThrow(Platform.getCurrentServerOrThrow().registryAccess());
    }

    /**
     * Uses current server to access the registry. Use with caution.
     */
    public Optional<O> get() {
        return get(Platform.getCurrentServerOrThrow().registryAccess());
    }

    // -- ResourceKey

    public boolean isFor(ResourceKey<? extends Registry<?>> registryKey) {
        return key.isFor(registryKey);
    }

    public <E> Optional<ResourceKey<E>> cast(ResourceKey<? extends Registry<E>> registryKey) {
        return key.cast(registryKey);
    }

    public ResourceLocation location() {
        return key.location();
    }

    public ResourceLocation registry() {
        return key.registry();
    }

    public ResourceKey<Registry<T>> registryKey() {
        return key.registryKey();
    }

    @Override
    public @NotNull String toString() {
        return key().toString();
    }

    public ResourceKey<T> key() {
        return key;
    }

    public Function<Holder<T>, O> toObject() {
        return objectConstructor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ResourceDefinition) obj;
        return Objects.equals(this.key, that.key) &&
              Objects.equals(this.objectConstructor, that.objectConstructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, objectConstructor);
    }
}
