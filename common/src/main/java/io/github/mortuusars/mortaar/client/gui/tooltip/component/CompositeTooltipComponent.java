package io.github.mortuusars.mortaar.client.gui.tooltip.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class CompositeTooltipComponent implements ClientTooltipComponent {
    private final List<ClientTooltipComponent> components;

    public CompositeTooltipComponent(List<ClientTooltipComponent> components) {
        this.components = components;
    }

    @Override
    public int getWidth(@NonNull Font font) {
        int width = 0;
        for (ClientTooltipComponent component : components) {
            width = Math.max(component.getWidth(font), width);
        }
        return width;
    }

    @Override
    public int getHeight(@NonNull Font font) {
        int height = 0;
        for (ClientTooltipComponent component : components) {
            height += component.getHeight(font);
        }
        return height;
    }

    @Override
    public void extractText(@NonNull GuiGraphicsExtractor graphics, @NonNull Font font, int x, int y) {
        for (ClientTooltipComponent component : components) {
            component.extractText(graphics, font, x, y);
            y += component.getHeight(font);
        }
    }

    @Override
    public void extractImage(@NonNull Font font, int x, int y, int w, int h, @NonNull GuiGraphicsExtractor graphics) {
        for (ClientTooltipComponent component : components) {
            component.extractImage(font, x, y, w, h, graphics);
            y += component.getHeight(font);
        }
    }
}
