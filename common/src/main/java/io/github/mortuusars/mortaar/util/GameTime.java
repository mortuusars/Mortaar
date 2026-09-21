package io.github.mortuusars.mortaar.util;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public interface GameTime {
    long getTick();

    default int getAsInt() {
        return (int) getTick();
    }

    static GameTime of(Level level) {
        return level::getGameTime;
    }

    static GameTime at(long tick) {
        return () -> tick;
    }

    // --

    default GameTime fromNow(GameTime time) {
        return () -> getTick() + time.getTick();
    }

    default GameTime fromNow(long ticks) {
        return () -> getTick() + ticks;
    }

    default boolean hasPassed(GameTime time) {
        return hasPassed(time.getTick());
    }

    default boolean hasPassed(long tick) {
        return getTick() > tick;
    }

    default GameTime elapsedSince(GameTime time) {
        return () -> getTick() - time.getTick();
    }

    default GameTime elapsedSince(long tick) {
        return () -> getTick() - tick;
    }

    default GameTime remainingTo(GameTime time) {
        return () -> time.getTick() - getTick();
    }

    default GameTime remainingTo(long tick) {
        return () -> tick - getTick();
    }

    // --

    default MutableComponent format(boolean withSeconds) {
        return format(getTick(), withSeconds);
    }

    default MutableComponent formatLargest(boolean withSeconds) {
        return formatLargest(getTick(), withSeconds);
    }

    // --

    static MutableComponent format(long ticks, boolean withSeconds) {
        long secondsTotal = ticks / 20;

        long years = secondsTotal / 31104000;
        secondsTotal %= 31104000;
        long months = secondsTotal / 2592000;
        secondsTotal %= 2592000;
        long weeks = secondsTotal / 604800;
        secondsTotal %= 604800;
        long days = secondsTotal / 86400;
        secondsTotal %= 86400;
        long hours = secondsTotal / 3600;
        secondsTotal %= 3600;
        long minutes = secondsTotal / 60;
        secondsTotal %= 60;
        long seconds = secondsTotal;

        List<Component> parts = new ArrayList<>();

        if (years > 0) parts.add(Component.translatable("gui.mortaar.game_time.years", years));
        if (months > 0) parts.add(Component.translatable("gui.mortaar.game_time.months", months));
        if (weeks > 0) parts.add(Component.translatable("gui.mortaar.game_time.weeks", weeks));
        if (days > 0) parts.add(Component.translatable("gui.mortaar.game_time.days", days));
        if (hours > 0) parts.add(Component.translatable("gui.mortaar.game_time.hours", hours));
        if (minutes > 0) parts.add(Component.translatable("gui.mortaar.game_time.minutes", minutes));
        if (withSeconds) {
            if (seconds > 0) parts.add(Component.translatable("gui.mortaar.game_time.seconds", seconds));
            else parts.add(Component.translatable("gui.mortaar.game_time.less_than_one_second"));
        }
        if (parts.isEmpty()) parts.add(Component.translatable("gui.mortaar.game_time.less_than_one_minute"));

        MutableComponent result = Component.empty();
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) result.append(CommonComponents.SPACE);
            result.append(parts.get(i));
        }

        return result;
    }

    static MutableComponent formatLargest(long ticks, boolean withSeconds) {
        long secondsTotal = ticks / 20;

        long years = secondsTotal / 31104000;
        if (years > 0) return Component.translatable("gui.mortaar.game_time.years", years);
        secondsTotal %= 31104000;
        long months = secondsTotal / 2592000;
        if (months > 0) return Component.translatable("gui.mortaar.game_time.months", months);
        secondsTotal %= 2592000;
        long weeks = secondsTotal / 604800;
        if (weeks > 0) return Component.translatable("gui.mortaar.game_time.weeks", weeks);
        secondsTotal %= 604800;
        long days = secondsTotal / 86400;
        if (days > 0) return Component.translatable("gui.mortaar.game_time.days", days);
        secondsTotal %= 86400;
        long hours = secondsTotal / 3600;
        if (hours > 0) return Component.translatable("gui.mortaar.game_time.hours", hours);
        secondsTotal %= 3600;
        long minutes = secondsTotal / 60;
        if (minutes > 0) return Component.translatable("gui.mortaar.game_time.minutes", minutes);
        secondsTotal %= 60;
        long seconds = secondsTotal;
        if (withSeconds) {
            if (seconds > 0) return Component.translatable("gui.mortaar.game_time.minutes", minutes);
            else return Component.translatable("gui.mortaar.game_time.less_than_one_second");
        }
        return Component.translatable("gui.mortaar.game_time.less_than_one_minute");
    }
}