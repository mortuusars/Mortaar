package io.github.mortuusars.mortaar.fabric;

import com.mojang.brigadier.arguments.ArgumentType;
import io.github.mortuusars.mortaar.Registrar;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistrarFabric implements Registrar {
    private final String modId;

    public RegistrarFabric(String modId) {
        this.modId = modId;
    }

    @Override
    public String getModId() {
        return modId;
    }

    public <T extends Block> Supplier<T> block(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, id), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> blockEntityType(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, id), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> newBlockEntityType(BlockEntitySupplier<T> blockEntitySupplier, Block... validBlocks) {
        return BlockEntityType.Builder.of(blockEntitySupplier::create, validBlocks).build();
    }

    @Override
    public Supplier<PoiType> poiType(ResourceKey<PoiType> key, int ticketCount, int searchDistance, Supplier<Set<BlockState>> states) {
        PoiType type = PointOfInterestHelper.register(key.location(), ticketCount, searchDistance, states.get());
        return () -> type;
    }

    @Override
    public <T extends Item> Supplier<T> item(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, id), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> creativeTab(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(modId, id), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> entityType(String id, EntityType.EntityFactory<T> factory,
                                                                        MobCategory category, float width, float height,
                                                                        int clientTrackingRange, boolean velocityUpdates, int updateInterval) {
        EntityType<T> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, id),
              EntityType.Builder.of(factory, category)
                    .sized(width, height)
                    .clientTrackingRange(clientTrackingRange)
                    .alwaysUpdateVelocity(velocityUpdates)
                    .updateInterval(updateInterval)
                    .build());
        return () -> type;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> entityType(String id, EntityType.EntityFactory<T> factory, MobCategory category, boolean receiveVelocityUpdates, Consumer<EntityType.Builder<T>> typeBuilder) {
        EntityType.Builder<T> builder = EntityType.Builder.of(factory, category);
        typeBuilder.accept(builder);
        builder.alwaysUpdateVelocity(receiveVelocityUpdates);
        EntityType<T> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, id), builder.build());
        return () -> type;
    }

    @Override
    public <T> Supplier<EntityDataSerializer<T>> entityDataSerializer(String id, EntityDataSerializer<T> serializer) {
        EntityDataSerializers.registerSerializer(serializer);
        return () -> serializer;
    }

    @Override
    public <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(modId, id), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> menuType(String id, MenuTypeSupplier<T> supplier) {
        ExtendedScreenHandlerType<T, byte[]> type = new ExtendedScreenHandlerType<>((syncId, inventory, data) -> {
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(data), inventory.player.registryAccess());
            T menu = supplier.create(syncId, inventory, buffer);
            buffer.release();
            return menu;
        }, ByteBufCodecs.BYTE_ARRAY.mapStream(Function.identity()));

        Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(modId, id), type);

        return () -> type;
    }

    @Override
    public <T extends Recipe<I>, I extends RecipeInput> Supplier<RecipeType<T>> recipeType(String id, Supplier<RecipeType<T>> supplier) {
        RecipeType<T> obj = Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(modId, id), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> recipeSerializer(String id, Supplier<RecipeSerializer<T>> supplier) {
        RecipeSerializer<T> obj = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(modId, id), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> criterionTrigger(String name, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.TRIGGER_TYPES, ResourceLocation.fromNamespaceAndPath(modId, name), supplier.get());
        return () -> obj;
    }

    @Override
    public <T extends ItemSubPredicate.Type<?>> Supplier<T> itemSubPredicate(String name, Supplier<T> supplier) {
        T obj = Registry.register(BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), supplier.get());
        return () -> obj;
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
    Supplier<ArgumentTypeInfo<A, T>> commandArgumentType(String id, Class<A> infoClass, I argumentTypeInfo) {
        ArgumentTypeRegistry.registerArgumentType(ResourceLocation.fromNamespaceAndPath(modId, id), infoClass, argumentTypeInfo);
        return () -> argumentTypeInfo;
    }

    @Override
    public <T extends FeatureConfiguration> Supplier<Feature<?>> worldGenFeature(String name, Supplier<Feature<T>> featureSupplier) {
        Feature<T> feature = Registry.register(BuiltInRegistries.FEATURE, name, featureSupplier.get());
        return () -> feature;
    }

    @Override
    public <T> DataComponentType<T> dataComponentType(String name, Consumer<DataComponentType.Builder<T>> builderConsumer) {
        var builder = DataComponentType.<T>builder();
        builderConsumer.accept(builder);
        var componentType = builder.build();
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(modId, name), componentType);
    }

    @Override
    public <T extends ParticleType<? extends ParticleOptions>> Supplier<T> particleType(String name, Supplier<T> supplier) {
        T particleType = Registry.register(BuiltInRegistries.PARTICLE_TYPE, name, supplier.get());
        return () -> particleType;
    }

    @Override
    public Supplier<ResourceLocation> stat(ResourceLocation location, StatFormatter formatter) {
        net.minecraft.core.Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
        net.minecraft.stats.Stats.CUSTOM.get(location, formatter);
        return () -> location;
    }
}
