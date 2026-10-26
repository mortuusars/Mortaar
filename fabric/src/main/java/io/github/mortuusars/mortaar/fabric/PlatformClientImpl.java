package io.github.mortuusars.mortaar.fabric;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class PlatformClientImpl {
    public static BakedModel getModel(ModelResourceLocation model) {
        // Fabric adds it's "fabric_resource" to id. NeoForge uses model location as is.
        return Minecraft.getInstance().getModelManager().getModel(model.id());
    }
}
