package io.github.mortuusars.mortaar;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.mortaar.bugger.Bugger;
import io.github.mortuusars.mortaar.bugger.network.ClientboundBuggerDataPacket;
import io.github.mortuusars.mortaar.bugger.test.cases.MortaarBuggerTests;
import io.github.mortuusars.mortaar.command.argument.GameTimeIdArgument;
import io.github.mortuusars.mortaar.util.supporter.Supporters;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class Mortaar {
    public static final String ID = "mortaar";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Registrar REGISTRAR = Register.registrar(ID);

    public static void init() {
        Bugger.enabler = () -> Config.Server.SPEC.isLoaded() && Config.Server.DEBUG_MODE.get();
        Bugger.addTests(MortaarBuggerTests::createTests);

        ArgumentTypes.init();

        Register.clientboundPacket(ClientboundBuggerDataPacket.TYPE, ClientboundBuggerDataPacket.STREAM_CODEC);

        // Query supporters early, so it will be available right away when needed
        Supporters.query();
    }

    /**
     * Creates resource location in the mod namespace with the given path.
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static class ArgumentTypes {
        public static final Supplier<ArgumentTypeInfo<GameTimeIdArgument, SingletonArgumentInfo<GameTimeIdArgument>.Template>> GAME_TIME_ID =
              REGISTRAR.commandArgumentType("game_time_id", GameTimeIdArgument.class, SingletonArgumentInfo.contextFree(GameTimeIdArgument::id));

        public static void init() {
        }
    }
}
