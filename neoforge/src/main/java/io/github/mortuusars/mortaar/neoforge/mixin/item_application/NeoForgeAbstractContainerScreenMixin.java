package io.github.mortuusars.mortaar.neoforge.mixin.item_application;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.mortuusars.mortaar.client.Minecrft;
import io.github.mortuusars.mortaar.world.item.ApplicationTargetItem;
import io.github.mortuusars.mortaar.world.item.ApplicatorItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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

    //TOOD: save as fabric. try moving into GuiGraphics

    @SuppressWarnings("LocalMayUseName")
    @WrapOperation(method = "renderSlotContents", at= @At(value = "INVOKE",
          target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V"))
    private void onRenderItemDecorations(GuiGraphicsExtractor instance, Font font, ItemStack itemStack, int x, int y,
                                         String countText, Operation<Void> original, @Local(argsOnly = true) Slot slot) {
        original.call(instance, font, itemStack, x, y, countText);

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
            instance.pose().pushMatrix();
            instance.text(font, "+", x + 19 - 2 - font.width("+"), y - 1, 0xFFFFD74E, true);
            instance.pose().popMatrix();
        }
    }
}
