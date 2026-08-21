package io.github.mortuusars.mortaar.advancement.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.mortaar.util.color.Color;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ColorPredicate(Optional<Integer> exact,
                             Optional<MinMaxBounds.Ints> red,
                             Optional<MinMaxBounds.Ints> green,
                             Optional<MinMaxBounds.Ints> blue,
                             Optional<MinMaxBounds.Ints> alpha,
                             Optional<MinMaxBounds.Doubles> hue,
                             Optional<MinMaxBounds.Doubles> saturation,
                             Optional<MinMaxBounds.Doubles> brightness) {
    public static final Codec<ColorPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(
          Codec.INT.optionalFieldOf("exact").forGetter(ColorPredicate::exact),
          MinMaxBounds.Ints.CODEC.optionalFieldOf("red").forGetter(ColorPredicate::red),
          MinMaxBounds.Ints.CODEC.optionalFieldOf("green").forGetter(ColorPredicate::green),
          MinMaxBounds.Ints.CODEC.optionalFieldOf("blue").forGetter(ColorPredicate::blue),
          MinMaxBounds.Ints.CODEC.optionalFieldOf("alpha").forGetter(ColorPredicate::alpha),
          MinMaxBounds.Doubles.CODEC.optionalFieldOf("hue").forGetter(ColorPredicate::hue),
          MinMaxBounds.Doubles.CODEC.optionalFieldOf("saturation").forGetter(ColorPredicate::saturation),
          MinMaxBounds.Doubles.CODEC.optionalFieldOf("brightness").forGetter(ColorPredicate::brightness)
    ).apply(i, ColorPredicate::new));

    public boolean matches(int colorARGB) {
        return (exact.isEmpty() || exact.get() == colorARGB)
              && (red.isEmpty() || red.get().matches(FastColor.ARGB32.red(colorARGB)))
              && (green.isEmpty() || green.get().matches(FastColor.ARGB32.green(colorARGB)))
              && (blue.isEmpty() || blue.get().matches(FastColor.ARGB32.blue(colorARGB)))
              && (alpha.isEmpty() || alpha.get().matches(FastColor.ARGB32.alpha(colorARGB)))
              && hsbMatches(colorARGB);
    }

    private boolean hsbMatches(int colorARGB) {
        if (hue().isEmpty() && saturation().isEmpty() && brightness().isEmpty()) {
            return true;
        }

        float[] hsb = Color.HSB.RGBtoHSB(Color.rgb(colorARGB));

        return (this.hue.isEmpty() || this.hue.get().matches(hsb[0]))
              && (this.saturation.isEmpty() || this.saturation.get().matches(hsb[1]))
              && (this.brightness.isEmpty() || this.brightness.get().matches(hsb[2]));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        @Nullable Integer exact;
        @Nullable MinMaxBounds.Ints red;
        @Nullable MinMaxBounds.Ints green;
        @Nullable MinMaxBounds.Ints blue;
        @Nullable MinMaxBounds.Ints alpha;
        @Nullable MinMaxBounds.Doubles hue;
        @Nullable MinMaxBounds.Doubles saturation;
        @Nullable MinMaxBounds.Doubles brightness;

        public Builder exact(@Nullable Integer exact) {
            this.exact = exact;
            return this;
        }

        public Builder red(@Nullable MinMaxBounds.Ints red) {
            this.red = red;
            return this;
        }

        public Builder green(@Nullable MinMaxBounds.Ints green) {
            this.green = green;
            return this;
        }

        public Builder blue(@Nullable MinMaxBounds.Ints blue) {
            this.blue = blue;
            return this;
        }

        public Builder alpha(@Nullable MinMaxBounds.Ints alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder hue(@Nullable MinMaxBounds.Doubles hue) {
            this.hue = hue;
            return this;
        }

        public Builder saturation(@Nullable MinMaxBounds.Doubles saturation) {
            this.saturation = saturation;
            return this;
        }

        public Builder brightness(@Nullable MinMaxBounds.Doubles brightness) {
            this.brightness = brightness;
            return this;
        }

        public ColorPredicate build() {
            return new ColorPredicate(
                  Optional.ofNullable(exact),
                  Optional.ofNullable(red),
                  Optional.ofNullable(green),
                  Optional.ofNullable(blue),
                  Optional.ofNullable(alpha),
                  Optional.ofNullable(hue),
                  Optional.ofNullable(saturation),
                  Optional.ofNullable(brightness)
            );
        }
    }
}
