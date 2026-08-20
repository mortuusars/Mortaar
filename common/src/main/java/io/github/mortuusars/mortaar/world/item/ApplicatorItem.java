package io.github.mortuusars.mortaar.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ApplicatorItem {
    default boolean shouldRenderSlotTooltipWhileCarrying(Level level, ItemStack carried, ItemStack hovered) {
        return false;
    }
}
