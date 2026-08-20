package io.github.mortuusars.mortaar;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Mortaar {
    public static final String ID = "mortaar";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
    }

    /**
     * Creates resource location in the mod namespace with the given path.
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
