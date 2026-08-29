package io.github.mortuusars.mortaar.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import io.github.mortuusars.mortaar.Config;
import io.github.mortuusars.mortaar.Mortaar;
import io.github.mortuusars.mortaar.command.MortaarCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;

public class MortaarFabric implements ModInitializer {
    // Server field to access when no other objects are available to get it from.
    public static @Nullable MinecraftServer server;

    @Override
    public void onInitialize() {
        Mortaar.init();
        NeoForgeConfigRegistry.INSTANCE.register(Mortaar.ID, ModConfig.Type.SERVER, Config.Server.SPEC);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            MortaarCommand.register(dispatcher, registryAccess);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> server = minecraftServer);
        ServerLifecycleEvents.SERVER_STOPPED.register(minecraftServer -> server = null);
    }
}
