package io.github.mortuusars.mortaar.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ApplicatorItem {
    default boolean canBeAppliedTo(Player player, AbstractContainerMenu menu, Slot slot, ItemStack carried) {
        return false;
    }

    default boolean shouldRenderSlotTooltipWhileCarrying(Player player, AbstractContainerMenu menu, Slot slot, ItemStack carried) {
        return canBeAppliedTo(player, menu, slot, carried);
    }

    default List<Component> addTooltipLinesToTarget(Player player, AbstractContainerMenu menu, Slot slot, ItemStack carried, List<Component> lines) {
        return lines;
    }
}
