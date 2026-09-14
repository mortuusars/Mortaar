package io.github.mortuusars.mortaar.neoforge;

import com.mojang.brigadier.arguments.ArgumentType;
import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.Registrar;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistrarNeoForge implements Registrar {
    private final String modId;

    public final DeferredRegister<Block> BLOCKS;
    public final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
    public final DeferredRegister<PoiType> POI_TYPES;
    public final DeferredRegister<Item> ITEMS;
    public final DeferredRegister<CreativeModeTab> CREATIVE_TABS;
    public final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS;
    public final DeferredRegister<MenuType<?>> MENU_TYPES;
    public final DeferredRegister<SoundEvent> SOUND_EVENTS;
    public final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    public final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public final DeferredRegister<CriterionTrigger<?>> CRITERION_TRIGGERS;
    public final DeferredRegister<ItemSubPredicate.Type<?>> ITEM_SUB_PREDICATES;
    public final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES;
    public final DeferredRegister<Feature<?>> WORLD_GEN_FEATURES;
    public final DeferredRegister.DataComponents DATA_COMPONENT_TYPES;
    public final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public final DeferredRegister<ResourceLocation> CUSTOM_STATS;
    public final Map<ResourceLocation, StatFormatter> STATS = new HashMap<>();

    public RegistrarNeoForge(String modId) {
        this.modId = modId;
        BLOCKS = DeferredRegister.create(Registries.BLOCK, modId);
        BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, modId);
        POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, modId);
        ITEMS = DeferredRegister.create(Registries.ITEM, modId);
        CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, modId);
        ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, modId);
        MENU_TYPES = DeferredRegister.create(Registries.MENU, modId);
        SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, modId);
        RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, modId);
        RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, modId);
        CRITERION_TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, modId);
        ITEM_SUB_PREDICATES = DeferredRegister.create(Registries.ITEM_SUB_PREDICATE_TYPE, modId);
        COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, modId);
        WORLD_GEN_FEATURES = DeferredRegister.create(Registries.FEATURE, modId);
        DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, modId);
        PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, modId);
        CUSTOM_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, modId);

        ModList.get().getModContainerById(modId).ifPresentOrElse(
              modContainer -> {
                  register(modContainer.getEventBus());
              },
              () -> Mortaar.LOGGER.error("Cannot register values from a registrar: ModContainer of '{}' is not found.", modId)
        );
    }

    @Override
    public String getModId() {
        return modId;
    }

    public void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        POI_TYPES.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        ENTITY_DATA_SERIALIZERS.register(modEventBus);
        ITEMS.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        CRITERION_TRIGGERS.register(modEventBus);
        ITEM_SUB_PREDICATES.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        COMMAND_ARGUMENT_TYPES.register(modEventBus);
        WORLD_GEN_FEATURES.register(modEventBus);
        DATA_COMPONENT_TYPES.register(modEventBus);
        PARTICLE_TYPES.register(modEventBus);
        CUSTOM_STATS.register(modEventBus);
    }

    // --

    @Override
    public <T extends Block> Supplier<T> block(String id, Supplier<T> supplier) {
        return BLOCKS.register(id, supplier);
    }

    @Override
    public <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> blockEntityType(String id, Supplier<T> sup) {
        return BLOCK_ENTITY_TYPES.register(id, sup);
    }

    @Override
    public Supplier<PoiType> poiType(ResourceKey<PoiType> key, int ticketCount, int searchDistance, Supplier<Set<BlockState>> states) {
        return POI_TYPES.register(key.location().getPath(), () -> new PoiType(states.get(), ticketCount, searchDistance));
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> newBlockEntityType(BlockEntitySupplier<T> blockEntitySupplier, Block... validBlocks) {
        return BlockEntityType.Builder.of(blockEntitySupplier::create, validBlocks).build(null);
    }

    @Override
    public <T extends Item> Supplier<T> item(String id, Supplier<T> supplier) {
        return ITEMS.register(id, supplier);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> creativeTab(String id, Supplier<T> supplier) {
        return CREATIVE_TABS.register(id, supplier);
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> entityType(String id, EntityType.EntityFactory<T> factory, MobCategory category,
                                                                        float width, float height, int clientTrackingRange, boolean velocityUpdates, int updateInterval) {
        return ENTITY_TYPES.register(id, () -> EntityType.Builder.of(factory, category)
              .sized(width, height)
              .clientTrackingRange(clientTrackingRange)
              .setShouldReceiveVelocityUpdates(velocityUpdates)
              .updateInterval(updateInterval)
              .build(id));
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> entityType(String id, EntityType.EntityFactory<T> factory, MobCategory category, boolean receiveVelocityUpdates, Consumer<EntityType.Builder<T>> typeBuilder) {
        return ENTITY_TYPES.register(id, () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(factory, category);
            builder.setShouldReceiveVelocityUpdates(receiveVelocityUpdates);
            typeBuilder.accept(builder);
            return builder.build(id);
        });
    }

    @Override
    public <T> Supplier<EntityDataSerializer<T>> entityDataSerializer(String id, EntityDataSerializer<T> serializer) {
        return ENTITY_DATA_SERIALIZERS.register(id, () -> serializer);
    }

    @Override
    public <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier) {
        return SOUND_EVENTS.register(id, supplier);
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> menuType(String id, MenuTypeSupplier<T> supplier) {
        return MENU_TYPES.register(id, () -> IMenuTypeExtension.create(supplier::create));
    }

    @Override
    public <T extends Recipe<I>, I extends RecipeInput> Supplier<RecipeType<T>> recipeType(String id, Supplier<RecipeType<T>> supplier) {
        return RECIPE_TYPES.register(id, supplier);
    }

    @Override
    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> recipeSerializer(String id, Supplier<RecipeSerializer<T>> supplier) {
        return RECIPE_SERIALIZERS.register(id, supplier);
    }

    @Override
    public <T extends CriterionTrigger<?>> Supplier<T> criterionTrigger(String name, Supplier<T> supplier) {
        return CRITERION_TRIGGERS.register(name, supplier);
    }

    @Override
    public <T extends ItemSubPredicate.Type<?>> Supplier<T> itemSubPredicate(String name, Supplier<T> supplier) {
        return ITEM_SUB_PREDICATES.register(name, supplier);
    }

    @Override
    public <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
    Supplier<ArgumentTypeInfo<A, T>> commandArgumentType(String id, Class<A> infoClass, I argumentTypeInfo) {
        return COMMAND_ARGUMENT_TYPES.register(id,
              () -> ArgumentTypeInfos.registerByClass(infoClass, argumentTypeInfo));
    }

    @Override
    public <T extends FeatureConfiguration> Supplier<Feature<?>> worldGenFeature(String name, Supplier<Feature<T>> featureSupplier) {
        return WORLD_GEN_FEATURES.register(name, featureSupplier);
    }

    @Override
    public <T> DataComponentType<T> dataComponentType(String name, Consumer<DataComponentType.Builder<T>> builderConsumer) {
        var builder = DataComponentType.<T>builder();
        builderConsumer.accept(builder);
        var componentType = builder.build();
        DATA_COMPONENT_TYPES.register(name, () -> componentType);
        return componentType;
    }

    @Override
    public <T extends ParticleType<? extends ParticleOptions>> Supplier<T> particleType(String name, Supplier<T> supplier) {
        return PARTICLE_TYPES.register(name, supplier);
    }

    @Override
    public Supplier<ResourceLocation> stat(ResourceLocation location, StatFormatter formatter) {
        STATS.put(location, formatter);
        return CUSTOM_STATS.register(location.getPath(), () -> location);
    }
}
