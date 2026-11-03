package io.github.mortuusars.mortaar.mixin.bugger;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DebugScreenOverlay.class)
public class ScreenOverlayMixin {
    //TODO: Implement
//    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
//    private void onRender(GuiGraphics guiGraphics, CallbackInfo ci) {
//        if (BuggerScreen.render(guiGraphics)) {
//            ci.cancel();
//        }
//    }
}
