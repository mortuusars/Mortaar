package io.github.mortuusars.mortaar.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.RegistryAccess;

import java.util.Objects;

public abstract class Minecrft {
    public static Minecraft get() {
        return Minecraft.getInstance();
    }

    /**
     * @deprecated Use playerOrThrow to avoid unexpected crashes.
     */
    @Deprecated(since = "1.7.7")
    public static LocalPlayer player() {
        return Objects.requireNonNull(get().player, "Player is not available.");
    }

    /**
     * @deprecated Use levelOrThrow to avoid unexpected crashes.
     */
    @Deprecated(since = "1.7.7")
    public static ClientLevel level() {
        return Objects.requireNonNull(get().level, "Level is not available.");
    }

    /**
     * @deprecated Use gameModeOrThrow to avoid unexpected crashes.
     */
    @Deprecated(since = "1.7.7")
    public static MultiPlayerGameMode gameMode() {
        return Objects.requireNonNull(get().gameMode, "GameMode is not available.");
    }

    /**
     * @since 1.7.7
     */
    public static LocalPlayer playerOrThrow() {
        return Objects.requireNonNull(get().player, "Player is not available.");
    }

    /**
     * @since 1.7.7
     */
    public static ClientLevel levelOrThrow() {
        return Objects.requireNonNull(get().level, "Level is not available.");
    }

    /**
     * @since 1.7.7
     */
    public static MultiPlayerGameMode gameModeOrThrow() {
        return Objects.requireNonNull(get().gameMode, "GameMode is not available.");
    }

    /**
     * @since 1.7.7
     */
    public static GameRenderer gameRenderer() {
        return get().gameRenderer;
    }

    public static Options options() {
        return get().options;
    }

    public static RegistryAccess registryAccess() {
        return level().registryAccess();
    }

    public static void execute(Runnable runnable) {
        get().execute(runnable);
    }

    public static void releaseUseButton() {
        options().keyUse.setDown(false);
    }

    /**
     * @since 1.7.7
     */
    public static void stopPlayerMovement() {
        // Stop player moving if movement key is held
        Minecrft.player().xxa = 0;
        Minecrft.player().yya = 0;
        Minecrft.player().zza = 0;
        Minecrft.player().setJumping(false);
        options().keyUp.setDown(false);
        options().keyDown.setDown(false);
        options().keyLeft.setDown(false);
        options().keyRight.setDown(false);
        options().keyJump.setDown(false);
    }
}
