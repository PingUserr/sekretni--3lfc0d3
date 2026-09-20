package me.alpha432.oyvey.features.gui.items;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class Item
        extends Feature {
    public static GuiGraphics context;
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    private boolean hidden;

    public Item(String name) {
        super(name);
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void drawScreen(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
    }

    public void update() {
    }

    public void onKeyTyped(String typedChar, int keyCode) {
    }

    public void onKeyPressed(int key) {
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public boolean setHidden(boolean hidden) {
        this.hidden = hidden;
        return this.hidden;
    }

    protected void drawString(String text, double x, double y, Color color) {
        drawString(text, x, y, color.hashCode());
    }

    protected void drawString(String text, double x, double y, int color) {
        drawString(text, x, y, color, this.width + 3);
    }

    protected void drawString(String text, double x, double y, int color, int areaWidth) {
        final float textWidth = OyVey.fontManager.getStringWidth(text);
        if (areaWidth > 0 && textWidth > (float) areaWidth) {
            final float overflow = textWidth - (float) areaWidth;
            final double timeSeconds = System.currentTimeMillis() / 1000.0;
            final double period = Math.max(overflow * 0.5, 3.0);
            final double t = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * (timeSeconds * 1.35) / period)) / 2.0 + 0.5;
            final int offsetX = (int) (t * overflow) - 1;

            context.enableScissor((int) x, 0, (int) (x + areaWidth), mc.getWindow().getGuiScaledHeight());
            OyVey.fontManager.drawString(context, text, (float) x - offsetX, (float) y, color);
            context.disableScissor();
        } else {
            OyVey.fontManager.drawString(context, text, (float) x, (float) y, color);
        }
    }

    protected void drawString(String label, String value, double x, double y, int labelColor, int valueColor) {
        drawString(label, value, x, y, labelColor, valueColor, this.width + 3);
    }

    protected void drawString(String label, String value, double x, double y, int labelColor, int valueColor, int areaWidth) {
        float labelWidth = OyVey.fontManager.getStringWidth(label);
        OyVey.fontManager.drawString(context, label, (float) x, (float) y, labelColor);

        float valX = (float) x + labelWidth;
        float valAreaWidth = Math.max(10.0f, (float) areaWidth - labelWidth);

        final float valueWidth = OyVey.fontManager.getStringWidth(value);
        if (valueWidth > valAreaWidth) {
            final float overflow = valueWidth - valAreaWidth;
            final double timeSeconds = System.currentTimeMillis() / 1000.0;
            final double period = Math.max(overflow * 0.5, 3.0);
            final double t = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * (timeSeconds * 1.35) / period)) / 2.0 + 0.5;
            final int offsetX = (int) (t * overflow) - 1;

            context.enableScissor((int) valX, 0, (int) (valX + valAreaWidth), mc.getWindow().getGuiScaledHeight());
            OyVey.fontManager.drawString(context, value, valX - offsetX, (float) y, valueColor);
            context.disableScissor();
        } else {
            OyVey.fontManager.drawString(context, value, valX, (float) y, valueColor);
        }
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return false;
    }
}