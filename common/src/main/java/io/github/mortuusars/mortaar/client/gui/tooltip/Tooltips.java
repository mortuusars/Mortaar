package io.github.mortuusars.mortaar.client.gui.tooltip;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import io.github.mortuusars.mortaar.client.gui.tooltip.component.CompositeTooltipComponent;
import io.github.mortuusars.mortaar.world.item.tooltip.CompositeTooltip;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class Tooltips {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<Class<? extends TooltipComponent>, Function<TooltipComponent, ClientTooltipComponent>> constructors = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends TooltipComponent> void register(Class<T> componentClass, Function<T, ClientTooltipComponent> constructor) {
        if (constructors.containsKey(componentClass)) {
            LOGGER.warn("{} already has a tooltip constructor registered. New constructor will replace the old one.", componentClass.getName());
        }
        constructors.put(componentClass, (Function<TooltipComponent, ClientTooltipComponent>)constructor);
    }

    public static @Nullable ClientTooltipComponent create(TooltipComponent component) {
        if (component instanceof CompositeTooltip(List<TooltipComponent> components)) {
            return new CompositeTooltipComponent(components.stream().map(ClientTooltipComponent::create).toList());
        }

        @Nullable Function<TooltipComponent, ClientTooltipComponent> constructor = constructors.get(component.getClass());
        if (constructor != null) {
            return constructor.apply(component);
        }

        return null;
    }

    // --

    public static <T> Map<T, Tooltip> createMap(List<T> values, Function<T, Component> convertFunc) {
        Preconditions.checkArgument(!values.isEmpty(), "values list must not be empty.");
        Map<T, Tooltip> map = new HashMap<>();
        for (T value : values) {
            map.put(value, Tooltip.create(convertFunc.apply(value)));
        }
        return map;
    }
}
