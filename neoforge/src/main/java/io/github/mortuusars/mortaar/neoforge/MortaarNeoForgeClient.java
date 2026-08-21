package io.github.mortuusars.mortaar.neoforge;

import io.github.mortuusars.mortaar.MortaarClient;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class MortaarNeoForgeClient {
    public static void init(ModContainer modContainer) {
        MortaarClient.init();
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
