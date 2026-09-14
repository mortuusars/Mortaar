package io.github.mortuusars.mortaar;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Inventory;
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
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Registrar {
    String getModId();

    <T extends Block> Supplier<T> block(String id, Supplier<T> supplier);

    <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> blockEntityType(String id, Supplier<T> sup);

    <T extends BlockEntity> BlockEntityType<T> newBlockEntityType(Registrar.BlockEntitySupplier<T> blockEntitySupplier, Block... validBlocks);

    Supplier<PoiType> poiType(ResourceKey<PoiType> key, int ticketCount, int searchDistance, Supplier<Set<BlockState>> states);

    <T extends Item> Supplier<T> item(String id, Supplier<T> supplier);

    <T extends CreativeModeTab> Supplier<T> creativeTab(String id, Supplier<T> supplier);

    <T extends Entity> Supplier<EntityType<T>> entityType(String id, EntityType.EntityFactory<T> factory,
                                                          MobCategory category, float width, float height,
                                                          int clientTrackingRange, boolean velocityUpdates, int updateInterval);

    <T extends Entity> Supplier<EntityType<T>> entityType(String id, EntityType.EntityFactory<T> factory, MobCategory category,
                                                          boolean receiveVelocityUpdates, Consumer<EntityType.Builder<T>> typeBuilder);

    <T> Supplier<EntityDataSerializer<T>> entityDataSerializer(String id, EntityDataSerializer<T> serializer);

    <T extends SoundEvent> Supplier<T> soundEvent(String id, Supplier<T> supplier);

    <T extends AbstractContainerMenu> Supplier<MenuType<T>> menuType(String id, Registrar.MenuTypeSupplier<T> supplier);

    <T extends Recipe<I>, I extends RecipeInput> Supplier<RecipeType<T>> recipeType(String id, Supplier<RecipeType<T>> supplier);

    <T extends Recipe<?>> Supplier<RecipeSerializer<T>> recipeSerializer(String id, Supplier<RecipeSerializer<T>> supplier);

    <T extends CriterionTrigger<?>> Supplier<T> criterionTrigger(String name, Supplier<T> supplier);

    <T extends ItemSubPredicate.Type<?>> Supplier<T> itemSubPredicate(String name, Supplier<T> supplier);

    <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, I extends ArgumentTypeInfo<A, T>>
        Supplier<ArgumentTypeInfo<A, T>> commandArgumentType(String id, Class<A> infoClass, I argumentTypeInfo);

    <T extends FeatureConfiguration> Supplier<Feature<?>> worldGenFeature(String name, Supplier<Feature<T>> featureSupplier);

    <T> DataComponentType<T> dataComponentType(String name, Consumer<DataComponentType.Builder<T>> builderConsumer);

    <T extends ParticleType<? extends ParticleOptions>> Supplier<T> particleType(String name, Supplier<T> supplier);

    Supplier<ResourceLocation> stat(ResourceLocation location, StatFormatter formatter);

    // --

    @FunctionalInterface
    interface BlockEntitySupplier<T extends BlockEntity> {
        @NotNull T create(BlockPos pos, BlockState state);
    }

    @FunctionalInterface
    interface MenuTypeSupplier<T extends AbstractContainerMenu> {
        @NotNull T create(int windowId, Inventory playerInv, RegistryFriendlyByteBuf extraData);
    }
}
