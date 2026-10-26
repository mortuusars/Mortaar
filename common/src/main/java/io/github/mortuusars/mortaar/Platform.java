package io.github.mortuusars.mortaar;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class Platform {
    @ExpectPlatform
    public static boolean isInDevEnv() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isClient() {
        throw new AssertionError();
    }

    public static boolean isDedicatedServer() {
        return !isClient();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoading(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Path getGameDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static @Nullable MinecraftServer getCurrentServer() {
        throw new AssertionError();
    }

    public static Optional<MinecraftServer> getServer() {
        return Optional.ofNullable(getCurrentServer());
    }

    public static @NotNull MinecraftServer getCurrentServerOrThrow() {
        return Objects.requireNonNull(getCurrentServer());
    }

    @ExpectPlatform
    public static void openMenu(ServerPlayer serverPlayer, MenuProvider menuProvider, Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
        throw new AssertionError();
    }

    public static class Client {
        public static final Set<ModelResourceLocation> CUSTOM_MODELS = new HashSet<>();

        public static ModelResourceLocation registerCustomModel(ResourceLocation location) {
            ModelResourceLocation modelLocation = new ModelResourceLocation(location, "standalone");
            CUSTOM_MODELS.add(modelLocation);
            return modelLocation;
        }

        @ExpectPlatform
        public static BakedModel getModel(ModelResourceLocation location) {
            throw new AssertionError();
        }

        public static BakedModel getModelWithOverrides(ModelResourceLocation location, ItemStack stack,
                                                       @Nullable Level level, @Nullable LivingEntity entity, int seed) {
            BakedModel model = getModel(location);
            ClientLevel clientLevel = level instanceof ClientLevel cLevel ? cLevel : null;
            return model.getOverrides().resolve(model, stack, clientLevel, entity, seed);
        }
    }
}
