package io.github.mortuusars.mortaar.util;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Game Time (based) Identifier<br>
 * Simpler alternative to UUID, for cases when short representation is more important than safety.
 */
public class GTID implements GameTime, Comparable<GTID> {
    public static final Codec<GTID> CODEC = Codec.STRING.comapFlatMap(GTID::parseFromHex, GTID::toString);
    public static final Codec<GTID> CODEC_DECIMAL = Codec.STRING.comapFlatMap(GTID::parseFromDecimal, GTID::toStringDecimal);

    public static final StreamCodec<ByteBuf, GTID> STREAM_CODEC = StreamCodec.composite(
          ByteBufCodecs.VAR_LONG, GTID::getTick,
          ByteBufCodecs.VAR_INT, GTID::getSuffix,
          GTID::new
    );

    protected static volatile long currentClientTick;
    protected static final AtomicInteger currentClientSuffix = new AtomicInteger();
    protected static volatile long currentServerTick;
    protected static final AtomicInteger currentServerSuffix = new AtomicInteger();

    protected final long tick;
    protected final int suffix;

    protected GTID(long tick, int suffix) {
        Preconditions.checkArgument(tick >= 0, "Tick must be >= 0. Value: " + tick);
        Preconditions.checkArgument(suffix >= 0, "Suffix must be >= 0. Value: " + suffix);
        this.tick = tick;
        this.suffix = suffix;
    }

    /**
     * Creates a GTID based on current gameTime tick.<br>
     * - Each consecutive ID created on the same tick will have a suffix to make it unique.<br>
     * - Client and Server has their own suffix tracking.<br><br>
     * Uniqueness is guaranteed only for "correct" flow of time. Each time gameTime tick changes - suffix resets.
     */
    public static GTID create(Level level) {
        return new GTID(level.getGameTime(), getAndUpdateSuffix(level.getGameTime(), level.isClientSide()));
    }

    /**
     * Note: Using this method can break uniqueness of {@link #create(Level)}, if different tick is passed. Avoid using this method if possible.<br><br>
     * Creates a GTID based on provided tick.<br>
     * - Each consecutive ID created on the same tick will have a suffix to make it unique.<br>
     * - Client and Server has their own suffix tracking.<br><br>
     * Uniqueness is guaranteed only for "correct" flow of time. Each time tick changes - suffix resets.
     */
    public static GTID createChecked(long tick, boolean clientSide) {
        return new GTID(tick, getAndUpdateSuffix(tick, clientSide));
    }

    /**
     *  Creates a GTID from provided values.<br>
     *  No attempts to make the ID unique is done here.
     */
    public static GTID createUncheked(long tick, int suffix) {
        return new GTID(tick, suffix);
    }

    /**
     *  Creates a GTID from provided value.<br>
     *  No attempts to make the ID unique is done here.
     */
    public static GTID createUncheked(long tick) {
        return createUncheked(tick, 0);
    }

    protected static int getAndUpdateSuffix(long tick, boolean clientSide) {
        if (clientSide) {
            if (tick != currentClientTick) {
                currentClientTick = tick;
                currentClientSuffix.set(0);
            }
            return currentClientSuffix.getAndIncrement();
        } else {
            if (tick != currentServerTick) {
                currentServerTick = tick;
                currentServerSuffix.set(0);
            }
            return currentServerSuffix.getAndIncrement();
        }
    }

    // --

    @Override
    public long getTick() {
        return tick;
    }

    public int getSuffix() {
        return suffix;
    }

    @Override
    public int compareTo(@NotNull GTID other) {
        Preconditions.checkNotNull(other);
        if (equals(other)) return 0;
        if (tick == other.tick) return Integer.compare(suffix, other.suffix);
        return Long.compare(tick, other.tick);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GTID id = (GTID) o;
        return tick == id.tick && suffix == id.suffix;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tick, suffix);
    }

    @Override
    public String toString() {
        return (suffix > 0
              ? Long.toHexString(tick) + "-" + Integer.toHexString(suffix)
              : Long.toHexString(tick)).toUpperCase();
    }

    public String toStringDecimal() {
        return (suffix > 0
              ? tick + "-" + suffix
              : Long.toString(tick)).toUpperCase();
    }

    // --

    /**
     * Parses GTID from hex-based string.<br>
     * Format is:<br>
     * - '7F7F7F-7F7F' (tick-suffix)<br>
     * - '7F7F7F' (only tick)
     */
    public static DataResult<GTID> parseFromHex(String input) {
        if (input == null || input.isBlank()) {
            return DataResult.error(() -> "GTID string is null or empty.");
        }

        int dashIndex = input.indexOf('-');

        if (dashIndex == -1) {
            try {
                long tick = Long.parseLong(input, 16);
                if (tick < 0) {
                    return DataResult.error(() -> "Tick must be larger or equal to 0: " + input);
                }
                return DataResult.success(new GTID(tick, 0));
            } catch (NumberFormatException e) {
                return DataResult.error(() -> "Invalid number in GTID: " + input + " - " + e.getMessage());
            }
        }

        try {
            String tickPart = input.substring(0, dashIndex);
            String suffixPart = input.substring(dashIndex + 1);

            long tick = Long.parseLong(tickPart, 16);
            if (tick < 0) {
                return DataResult.error(() -> "Tick must be larger or equal to 0: " + input);
            }
            int suffix = Integer.parseInt(suffixPart, 16);
            if (suffix < 0) {
                return DataResult.error(() -> "Suffix must be larger or equal to 0: " + input);
            }
            return DataResult.success(new GTID(tick, suffix));
        } catch (Exception e) {
            return DataResult.error(() -> "Invalid input for GTID: " + input + " - " + e.getMessage());
        }
    }

    public static DataResult<GTID> parseFromDecimal(String input) {
        if (input == null || input.isBlank()) {
            return DataResult.error(() -> "GTID string is null or empty.");
        }

        int dashIndex = input.indexOf('-');

        if (dashIndex == -1) {
            try {
                long tick = Long.parseLong(input);
                if (tick < 0) {
                    return DataResult.error(() -> "Tick must be larger or equal to 0: " + input);
                }
                return DataResult.success(new GTID(tick, 0));
            } catch (NumberFormatException e) {
                return DataResult.error(() -> "Invalid number in GTID: " + input + " - " + e.getMessage());
            }
        }

        try {
            String tickPart = input.substring(0, dashIndex);
            String suffixPart = input.substring(dashIndex + 1);

            long tick = Long.parseLong(tickPart);
            if (tick < 0) {
                return DataResult.error(() -> "Tick must be larger or equal to 0: " + input);
            }
            int suffix = Integer.parseInt(suffixPart);
            if (suffix < 0) {
                return DataResult.error(() -> "Suffix must be larger or equal to 0: " + input);
            }
            return DataResult.success(new GTID(tick, suffix));
        } catch (Exception e) {
            return DataResult.error(() -> "Invalid input for GTID: " + input + " - " + e.getMessage());
        }
    }
}