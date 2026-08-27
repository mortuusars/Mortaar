package io.github.mortuusars.mortaar.world.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ApplicationTargetItem {
    /**
     * @deprecated Use fuller version {@link #shouldRenderSlotTooltipWhileCarrying(Player, AbstractContainerMenu, Slot, ItemStack)} instead.
     */
    @Deprecated(since = "1.2.2", forRemoval = true)
    default boolean shouldRenderSlotTooltipWhileCarrying(Level level, ItemStack carried, ItemStack hovered) {
        return false;
    }

    default boolean shouldRenderSlotTooltipWhileCarrying(Player player, AbstractContainerMenu menu, Slot slot, ItemStack carried) {
        return false;
    }
}
