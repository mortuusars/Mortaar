package io.github.mortuusars.mortaar.neoforge.mixin.item_application;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.mortuusars.mortaar.client.Minecrft;
import io.github.mortuusars.mortaar.world.item.ApplicationTargetItem;
import io.github.mortuusars.mortaar.world.item.ApplicatorItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerScreen.class)
public abstract class NeoForgeAbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen {
    protected NeoForgeAbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    public abstract T getMenu();

    @WrapOperation(method = "renderSlotContents", at= @At(value = "INVOKE",
          target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    private void onRenderItemDecorations(GuiGraphics instance, Font font, ItemStack stack, int x, int y,
                                         String text, Operation<Void> original, @Local(argsOnly = true) Slot slot) {
        original.call(instance, font, stack, x, y, text);

        ItemStack carried = getMenu().getCarried();
        if (carried.isEmpty()) {
            return;
        }

        boolean shouldDraw = false;

        if (carried.getItem() instanceof ApplicatorItem applicator
              && applicator.canBeAppliedTo(Minecrft.player(), getMenu(), slot, carried)) {
            shouldDraw = true;
        }

        if (slot.getItem().getItem() instanceof ApplicationTargetItem applicationTarget
              && applicationTarget.canApplyCarried(Minecrft.player(), getMenu(), slot, carried)) {
            shouldDraw = true;
        }

        if (shouldDraw) {
            instance.pose().pushPose();
            instance.pose().translate(0.0F, 0.0F, 200.0F);
            instance.drawString(font, "+", x + 19 - 2 - font.width("+"), y - 1, 0xFFFFD74E, true);
            instance.pose().popPose();
        }
    }
}
