package io.github.mortuusars.mortaar.mixin.bugger;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.mortuusars.mortaar.bugger.screen.BuggerEntityOverhead;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @WrapOperation(method = "extractNameTags(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FDD)V",
          at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;belowNameDisplay()Lnet/minecraft/network/chat/Component;"))
    private Component belowNameDisplay(Entity instance, Operation<Component> original) {
        @Nullable Component component = original.call(instance);

        List<Component> lines = BuggerEntityOverhead.getOverheadEntityInfoLines(instance);
        if (lines.isEmpty()) {
            return component;
        }

        MutableComponent result = Component.empty();
        if (component != null) {
            result.append(component);
            result.append(CommonComponents.NEW_LINE);
        }

        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            result.append(line);
            if (i == lines.size() - 1) {
                result.append(CommonComponents.NEW_LINE);
            }
        }

        return result;
    }
}
