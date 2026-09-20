package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.ClickGuiModule;
import me.alpha432.oyvey.util.render.font.CustomFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.io.InputStream;

public class FontManager {
    private CustomFont customFont;
    private boolean customFontEnabled = true;

    public void init() {
        if (this.customFont != null) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getTextureManager() == null) {
            return;
        }
        try (InputStream in = FontManager.class.getResourceAsStream("/assets/oyvey/font/lexend.ttf")) {
            if (in != null) {
                byte[] data = in.readAllBytes();
                this.customFont = new CustomFont(data, 18.0f, 0.5f, "lexend");
                OyVey.LOGGER.info("Successfully loaded custom font lexend.ttf");
            } else {
                try (InputStream inBackup = FontManager.class.getResourceAsStream("/assets/oyvey/fonts/lexend.ttf")) {
                    if (inBackup != null) {
                        byte[] data = inBackup.readAllBytes();
                        this.customFont = new CustomFont(data, 18.0f, 0.5f, "lexend");
                        OyVey.LOGGER.info("Successfully loaded custom font lexend.ttf from backup");
                    } else {
                        OyVey.LOGGER.warn("Could not find /assets/oyvey/font/lexend.ttf in resources");
                    }
                }
            }
        } catch (Throwable e) {
            OyVey.LOGGER.error("Failed to initialize custom font", e);
        }
    }

    private void ensureLoaded() {
        if (this.customFont == null) {
            init();
        }
    }

    public float drawString(GuiGraphics graphics, String text, float x, float y, int color) {
        return drawString(graphics, text, x, y, color, isShadow());
    }

    public float drawString(GuiGraphics graphics, String text, float x, float y, int color, boolean shadow) {
        ensureLoaded();
        if (isCustomFont() && this.customFont != null) {
            return this.customFont.drawString(graphics, text, x, y, color, shadow);
        }
        graphics.drawString(Minecraft.getInstance().font, text, (int) x, (int) y, color, shadow);
        return x + Minecraft.getInstance().font.width(text);
    }

    public float drawString(GuiGraphics graphics, String text, double x, double y, int color) {
        return drawString(graphics, text, (float) x, (float) y, color, isShadow());
    }

    public float drawString(GuiGraphics graphics, String text, double x, double y, int color, boolean shadow) {
        return drawString(graphics, text, (float) x, (float) y, color, shadow);
    }

    public float drawString(GuiGraphics graphics, String text, float x, float y, Color color) {
        return drawString(graphics, text, x, y, color.getRGB(), isShadow());
    }

    public float drawString(GuiGraphics graphics, String text, float x, float y, Color color, boolean shadow) {
        return drawString(graphics, text, x, y, color.getRGB(), shadow);
    }

    public float drawString(GuiGraphics graphics, String text, double x, double y, Color color) {
        return drawString(graphics, text, (float) x, (float) y, color.getRGB(), isShadow());
    }

    public float drawString(GuiGraphics graphics, String text, double x, double y, Color color, boolean shadow) {
        return drawString(graphics, text, (float) x, (float) y, color.getRGB(), shadow);
    }

    public float drawString(GuiGraphics graphics, Component component, float x, float y, int color) {
        return drawString(graphics, component, x, y, color, isShadow());
    }

    public float drawString(GuiGraphics graphics, Component component, float x, float y, int color, boolean shadow) {
        ensureLoaded();
        if (isCustomFont() && this.customFont != null) {
            return this.customFont.drawString(graphics, component, x, y, color, shadow);
        }
        graphics.drawString(Minecraft.getInstance().font, component, (int) x, (int) y, color, shadow);
        return x + Minecraft.getInstance().font.width(component);
    }

    public float drawString(GuiGraphics graphics, Component component, double x, double y, int color) {
        return drawString(graphics, component, (float) x, (float) y, color, isShadow());
    }

    public float drawString(GuiGraphics graphics, Component component, double x, double y, int color, boolean shadow) {
        return drawString(graphics, component, (float) x, (float) y, color, shadow);
    }

    public float drawString(GuiGraphics graphics, Component component, float x, float y, Color color) {
        return drawString(graphics, component, x, y, color.getRGB(), isShadow());
    }

    public float drawString(GuiGraphics graphics, Component component, float x, float y, Color color, boolean shadow) {
        return drawString(graphics, component, x, y, color.getRGB(), shadow);
    }

    public float drawString(GuiGraphics graphics, Component component, double x, double y, Color color) {
        return drawString(graphics, component, (float) x, (float) y, color.getRGB(), isShadow());
    }

    public float drawString(GuiGraphics graphics, Component component, double x, double y, Color color, boolean shadow) {
        return drawString(graphics, component, (float) x, (float) y, color.getRGB(), shadow);
    }

    public float drawStringWithShadow(GuiGraphics graphics, String text, float x, float y, int color) {
        return drawString(graphics, text, x, y, color, true);
    }

    public float drawStringWithShadow(GuiGraphics graphics, String text, double x, double y, int color) {
        return drawString(graphics, text, (float) x, (float) y, color, true);
    }

    public float drawStringWithShadow(GuiGraphics graphics, Component component, float x, float y, int color) {
        return drawString(graphics, component, x, y, color, true);
    }

    public float drawStringWithShadow(GuiGraphics graphics, Component component, double x, double y, int color) {
        return drawString(graphics, component, (float) x, (float) y, color, true);
    }

    public float drawCenteredString(GuiGraphics graphics, String text, float x, float y, int color) {
        return drawCenteredString(graphics, text, x, y, color, isShadow());
    }

    public float drawCenteredString(GuiGraphics graphics, String text, float x, float y, int color, boolean shadow) {
        float w = getStringWidth(text);
        return drawString(graphics, text, x - w / 2.0f, y, color, shadow);
    }

    public float drawCenteredString(GuiGraphics graphics, String text, double x, double y, int color) {
        return drawCenteredString(graphics, text, (float) x, (float) y, color, isShadow());
    }

    public float drawCenteredString(GuiGraphics graphics, String text, double x, double y, int color, boolean shadow) {
        return drawCenteredString(graphics, text, (float) x, (float) y, color, shadow);
    }

    public float drawCenteredString(GuiGraphics graphics, Component component, float x, float y, int color) {
        return drawCenteredString(graphics, component, x, y, color, isShadow());
    }

    public float drawCenteredString(GuiGraphics graphics, Component component, float x, float y, int color, boolean shadow) {
        float w = getStringWidth(component);
        return drawString(graphics, component, x - w / 2.0f, y, color, shadow);
    }

    public float drawCenteredString(GuiGraphics graphics, Component component, double x, double y, int color) {
        return drawCenteredString(graphics, component, (float) x, (float) y, color, isShadow());
    }

    public float drawCenteredString(GuiGraphics graphics, Component component, double x, double y, int color, boolean shadow) {
        return drawCenteredString(graphics, component, (float) x, (float) y, color, shadow);
    }

    public float drawCenteredStringWithShadow(GuiGraphics graphics, String text, float x, float y, int color) {
        return drawCenteredString(graphics, text, x, y, color, true);
    }

    public float drawCenteredStringWithShadow(GuiGraphics graphics, String text, double x, double y, int color) {
        return drawCenteredString(graphics, text, (float) x, (float) y, color, true);
    }

    public float drawCenteredStringWithShadow(GuiGraphics graphics, Component component, float x, float y, int color) {
        return drawCenteredString(graphics, component, x, y, color, true);
    }

    public float drawCenteredStringWithShadow(GuiGraphics graphics, Component component, double x, double y, int color) {
        return drawCenteredString(graphics, component, (float) x, (float) y, color, true);
    }

    public float getStringWidth(String text) {
        ensureLoaded();
        if (isCustomFont() && this.customFont != null) {
            return this.customFont.getStringWidth(text);
        }
        return Minecraft.getInstance().font.width(text);
    }

    public float getStringWidth(Component component) {
        ensureLoaded();
        if (isCustomFont() && this.customFont != null) {
            return this.customFont.getStringWidth(component);
        }
        return Minecraft.getInstance().font.width(component);
    }

    public int getFontHeight() {
        ensureLoaded();
        if (isCustomFont() && this.customFont != null) {
            return this.customFont.getFontHeight();
        }
        return Minecraft.getInstance().font.lineHeight;
    }

    public boolean isCustomFont() {
        if (ClickGuiModule.getInstance() != null) {
            return ClickGuiModule.getInstance().font.getValue();
        }
        return this.customFontEnabled;
    }

    public void setCustomFont(boolean customFontEnabled) {
        this.customFontEnabled = customFontEnabled;
    }

    public boolean isShadow() {
        if (ClickGuiModule.getInstance() != null) {
            return ClickGuiModule.getInstance().shadow.getValue();
        }
        return true;
    }

    public CustomFont getCustomFont() {
        return this.customFont;
    }
}
