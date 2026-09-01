package io.github.mortuusars.mortaar.world.item.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TooltipComponent that allows storing and rendering multiple components one after another.<br><br>
 * Doesn't need to be registered by each mod with {@link io.github.mortuusars.mortaar.client.gui.tooltip.Tooltips#register}, as Mortaar registers it by default. It's children must be registered as usual though.
 */
public record CompositeTooltip(List<TooltipComponent> components) implements TooltipComponent {
    @SafeVarargs
    public static Optional<TooltipComponent> of(Optional<TooltipComponent>... components) {
        if (components.length == 0) {
            return Optional.empty();
        }

        List<TooltipComponent> list = Arrays.stream(components).filter(Optional::isPresent).map(Optional::get).toList();
        if (list.isEmpty()) {
            return Optional.empty();
        }

        if (list.size() == 1) {
            return Optional.of(list.getFirst());
        }

        return Optional.of(new CompositeTooltip(list));
    }
}
