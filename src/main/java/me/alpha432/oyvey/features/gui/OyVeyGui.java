package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.items.Item;
import me.alpha432.oyvey.features.gui.items.buttons.ModuleButton;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class OyVeyGui extends Screen {
    private static OyVeyGui INSTANCE;
    private static Color colorClipboard = null;

    static {
        INSTANCE = new OyVeyGui();
    }

    private final ArrayList<Widget> widgets = new ArrayList<>();

    public OyVeyGui() {
        super(Component.literal("OyVey"));
        setInstance();
        load();
    }

    public static OyVeyGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OyVeyGui();
        }
        return INSTANCE;
    }

    public static OyVeyGui getClickGui() {
        return OyVeyGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        int x = -84;
        for (Module.Category category : OyVey.moduleManager.getCategories()) {
            if (category == Module.Category.HUD) continue;
            Widget panel = new Widget(category.getName(), x += 90, 4, true);
            OyVey.moduleManager.stream()
                    .filter(m -> m.getCategory() == category && !m.hidden)
                    .map(ModuleButton::new)
                    .forEach(panel::addButton);
            this.widgets.add(panel);
        }
        this.widgets.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    @Override
    protected void init() {
        super.init();
        centerWidgets();
    }

    public void centerWidgets() {
        if (this.widgets.isEmpty() || this.width <= 0 || this.height <= 0) {
            return;
        }
        for (Widget widget : this.widgets) {
            if (widget.isDragged()) {
                return;
            }
        }
        int totalWidth = (this.widgets.size() - 1) * 90 + this.widgets.get(0).getWidth();
        int startX = Math.max(10, (this.width - totalWidth) / 2);

        int maxHeight = 0;
        for (Widget widget : this.widgets) {
            int h = widget.getTotalHeight();
            if (h > maxHeight) {
                maxHeight = h;
            }
        }
        int startY = Math.max(20, (this.height - maxHeight) / 2);

        for (int i = 0; i < this.widgets.size(); i++) {
            Widget widget = this.widgets.get(i);
            widget.setX(startX + i * 90);
            widget.setY(startY);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Item.context = context;
        context.fill(0, 0, context.guiWidth(), context.guiHeight(), new Color(0, 0, 0, 120).hashCode());
        this.widgets.forEach(components -> components.drawScreen(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        this.widgets.forEach(components -> components.mouseClicked((int) click.x(), (int) click.y(), click.button()));
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        this.widgets.forEach(components -> components.mouseReleased((int) click.x(), (int) click.y(), click.button()));
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount < 0) {
            this.widgets.forEach(component -> {
                component.setY(component.getY() - 10);
                component.setDragged(true);
            });
        } else if (verticalAmount > 0) {
            this.widgets.forEach(component -> {
                component.setY(component.getY() + 10);
                component.setDragged(true);
            });
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        this.widgets.forEach(component -> component.onKeyPressed(input.input()));
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
        this.widgets.forEach(component -> component.onKeyTyped(input.codepointAsString(), input.modifiers()));
        return super.charTyped(input);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
    }

    public final ArrayList<Widget> getComponents() {
        return this.widgets;
    }

    public int getTextOffset() {
        return -6;
    }

    public static Color getColorClipboard() {
        return colorClipboard;
    }

    public static void setColorClipboard(Color color) {
        colorClipboard = color;
    }
}
