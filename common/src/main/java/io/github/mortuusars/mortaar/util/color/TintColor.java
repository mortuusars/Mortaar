package io.github.mortuusars.mortaar.util.color;

import com.mojang.serialization.Codec;
import io.github.mortuusars.mortaar.serialization.Codecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

/**
 * Represents modification to the color to tint it into various shades.<br>
 * Values larger than 1.0f will brighten the channel, lower than 1.0f - darken.
 * <br><br>
 * Created primarily to make working with RenderSystem#setShaderColor easier.<br>
 * Previewing the effect in Photoshop can be done with a Channel Mixer, by setting each channel to it's corresponding percentage (r = 1.32f -> Red = 132%).
 */
public record TintColor(float r, float g, float b, float a) {
    public static final Codec<TintColor> CODEC = Codecs.HEX_COLOR.xmap(TintColor::of, TintColor::tint);
    public static final StreamCodec<ByteBuf, TintColor> STREAM_CODEC = ByteBufCodecs.INT.map(TintColor::of, TintColor::tint);

    /**
     * Creates a TintColor from packet color.<br>
     * #7F7F7F corresponds to r=1, g=1, b=1, a=1, ie. untinted.
     */
    public static TintColor of(int argb) {
        float a = (float) (FastColor.ARGB32.alpha(argb) - 127) / 127 + 1;
        float r = (float) (FastColor.ARGB32.red(argb) - 127) / 127 + 1;
        float g = (float) (FastColor.ARGB32.green(argb) - 127) / 127 + 1;
        float b = (float) (FastColor.ARGB32.blue(argb) - 127) / 127 + 1;
        return new TintColor(r, g, b, a);
    }

    /**
     * Tints the neutral color.
     */
    public int tint() {
        return tint(0xFF7F7F7F);
    }

    /**
     * Tints the packed color.
     */
    public int tint(int argb) {
        int a = (int)Mth.clamp(FastColor.ARGB32.alpha(argb) * this.a, 0, 255);
        int r = (int)Mth.clamp(FastColor.ARGB32.red(argb) * this.r, 0, 255);
        int g = (int)Mth.clamp(FastColor.ARGB32.green(argb) * this.g, 0, 255);
        int b = (int)Mth.clamp(FastColor.ARGB32.blue(argb) * this.b, 0, 255);
        return FastColor.ARGB32.color(a, r, g, b);
    }
}
