package io.github.mortuusars.mortaar.client.gui;

import com.google.common.base.Preconditions;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Sprites {
    public static WidgetSprites normalOnly(Identifier base) {
        return new WidgetSprites(base, base);
    }

    public static WidgetSprites normalAndHighlighted(Identifier base) {
        return new WidgetSprites(base, base,
                Identifier.fromNamespaceAndPath(base.getNamespace(), base.getPath() + "_highlighted"));
    }

    public static WidgetSprites normalAndHighlighted(Identifier normal, Identifier highlighted) {
        return new WidgetSprites(normal, normal, highlighted);
    }

    public static WidgetSprites threeStates(Identifier base) {
        return new WidgetSprites(base,
                Identifier.fromNamespaceAndPath(base.getNamespace(), base.getPath() + "_disabled"),
                Identifier.fromNamespaceAndPath(base.getNamespace(), base.getPath() + "_highlighted"));
    }

    public static <T> Map<T, WidgetSprites> createMap(List<T> values, Function<T, WidgetSprites> convertFunc) {
        Preconditions.checkArgument(!values.isEmpty(), "values list must not be empty.");
        Map<T, WidgetSprites> map = new HashMap<>();
        for (T value : values) {
            map.put(value, convertFunc.apply(value));
        }
        return map;
    }
}
