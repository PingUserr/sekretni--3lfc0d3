package me.alpha432.oyvey.util.render.font;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CustomFont {
    private static final int ATLAS = 1024;
    private static final int PAD = 2;
    private static final int[] COLOR_CODES = new int[32];

    static {
        for (int i = 0; i < 32; ++i) {
            int base = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + base;
            int g = (i >> 1 & 1) * 170 + base;
            int b = (i & 1) * 170 + base;
            if (i == 6) {
                r += 85;
            }
            if (i >= 16) {
                r /= 4;
                g /= 4;
                b /= 4;
            }
            COLOR_CODES[i] = (r & 255) << 16 | (g & 255) << 8 | (b & 255);
        }
    }

    private final Font font;
    private final FontRenderContext frc;
    private final FontMetrics fm;
    private final float scale;
    private final float ascent;
    private final float descent;
    private final float lineHeight;
    private final float baselineOffset;
    private final Identifier textureId;
    private final NativeImage image;
    private final DynamicTexture texture;
    private final Map<Character, Glyph> glyphs = new HashMap<>();

    private int penX = PAD;
    private int penY = PAD;
    private int shelfHeight = 0;
    private boolean dirty = false;

    public CustomFont(byte[] fontData, float size, float scale, String name) {
        this.scale = scale;
        if (System.getProperty("java.awt.headless") == null) {
            System.setProperty("java.awt.headless", "true");
        }

        Font base;
        try {
            base = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(fontData));
        } catch (Exception e) {
            base = new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
        }

        this.font = base.deriveFont(size);
        this.frc = new FontRenderContext(new AffineTransform(), true, true);

        BufferedImage probe = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = probe.createGraphics();
        this.fm = g.getFontMetrics(this.font);
        this.ascent = this.fm.getAscent();
        this.descent = this.fm.getDescent();
        this.lineHeight = this.fm.getHeight();
        this.baselineOffset = this.ascent - 5.0f;
        g.dispose();

        this.image = new NativeImage(NativeImage.Format.RGBA, ATLAS, ATLAS, false);
        this.image.fillRect(0, 0, ATLAS, ATLAS, 0);
        this.textureId = Identifier.fromNamespaceAndPath("oyvey", "font/" + name.toLowerCase());
        this.texture = new DynamicTexture(() -> "oyvey/" + name, this.image);
        Minecraft.getInstance().getTextureManager().register(this.textureId, this.texture);

        prebake();
    }

    private void prebake() {
        for (char c = 0x20; c <= 0x7E; c++) {
            glyph(c);
        }
        for (char c = 0xA0; c <= 0xFF; c++) {
            glyph(c);
        }
        for (char c = 0x400; c <= 0x45F; c++) {
            glyph(c);
        }
        String symbols = "–—‘’“”•…→·«»№";
        for (int i = 0; i < symbols.length(); i++) {
            glyph(symbols.charAt(i));
        }
        flush();
    }

    private Glyph glyph(char c) {
        Glyph cached = this.glyphs.get(c);
        if (cached != null) {
            return cached;
        }
        Glyph baked = bake(c);
        this.glyphs.put(c, baked);
        return baked;
    }

    private Glyph bake(char c) {
        GlyphVector gv = this.font.createGlyphVector(this.frc, new char[]{c});
        float advance = (float) gv.getGlyphMetrics(0).getAdvanceX();
        if (advance <= 0) {
            advance = this.fm.charWidth(c);
        }

        Shape outline = gv.getGlyphOutline(0);
        Rectangle2D b = outline.getBounds2D();
        if (b.getWidth() <= 0.0 || b.getHeight() <= 0.0) {
            return new Glyph(0, 0, 0, 0, 0, 0, advance);
        }

        int offX = (int) Math.floor(b.getX()) - 1;
        int offY = (int) Math.floor(b.getY()) - 1;
        int w = (int) Math.ceil(b.getMaxX()) - offX + 1;
        int h = (int) Math.ceil(b.getMaxY()) - offY + 1;

        if (this.penX + w + PAD > ATLAS) {
            this.penX = PAD;
            this.penY += this.shelfHeight + PAD;
            this.shelfHeight = 0;
        }
        if (this.penY + h + PAD > ATLAS) {
            return new Glyph(0, 0, 0, 0, 0, 0, advance);
        }

        int u = this.penX;
        int v = this.penY;
        this.penX += w + PAD;
        this.shelfHeight = Math.max(this.shelfHeight, h);

        BufferedImage cell = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = cell.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.translate(-offX, -offY);
        g2d.setColor(Color.WHITE);
        g2d.fill(outline);
        g2d.dispose();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                this.image.setPixel(u + x, v + y, cell.getRGB(x, y));
            }
        }
        this.dirty = true;
        return new Glyph(u, v, w, h, offX, offY, advance);
    }

    private void flush() {
        if (this.dirty) {
            this.texture.upload();
            this.dirty = false;
        }
    }

    public float drawString(GuiGraphics graphics, String text, float x, float y, int color) {
        return drawString(graphics, text, x, y, color, false);
    }

    public float drawString(GuiGraphics graphics, String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            draw(graphics, text, x + 1.0f, y + 1.0f, color, true);
        }
        return draw(graphics, text, x, y, color, false);
    }

    public float drawStringWithShadow(GuiGraphics graphics, String text, float x, float y, int color) {
        return drawString(graphics, text, x, y, color, true);
    }

    public float drawCenteredString(GuiGraphics graphics, String text, float x, float y, int color) {
        float w = getStringWidth(text);
        return drawString(graphics, text, x - w / 2.0f, y, color, false);
    }

    public float drawCenteredString(GuiGraphics graphics, String text, float x, float y, int color, boolean shadow) {
        float w = getStringWidth(text);
        return drawString(graphics, text, x - w / 2.0f, y, color, shadow);
    }

    public float drawCenteredStringWithShadow(GuiGraphics graphics, String text, float x, float y, int color) {
        return drawCenteredString(graphics, text, x, y, color, true);
    }

    public float drawString(GuiGraphics graphics, Component component, float x, float y, int color) {
        return drawString(graphics, component, x, y, color, false);
    }

    public float drawString(GuiGraphics graphics, Component component, float x, float y, int color, boolean shadow) {
        if (shadow) {
            draw(graphics, component, x + 1.0f, y + 1.0f, color, true);
        }
        return draw(graphics, component, x, y, color, false);
    }

    public float drawStringWithShadow(GuiGraphics graphics, Component component, float x, float y, int color) {
        return drawString(graphics, component, x, y, color, true);
    }

    public float drawCenteredString(GuiGraphics graphics, Component component, float x, float y, int color) {
        float w = getStringWidth(component);
        return drawString(graphics, component, x - w / 2.0f, y, color, false);
    }

    public float drawCenteredString(GuiGraphics graphics, Component component, float x, float y, int color, boolean shadow) {
        float w = getStringWidth(component);
        return drawString(graphics, component, x - w / 2.0f, y, color, shadow);
    }

    public float drawCenteredStringWithShadow(GuiGraphics graphics, Component component, float x, float y, int color) {
        return drawCenteredString(graphics, component, x, y, color, true);
    }

    private float draw(GuiGraphics graphics, String text, float x, float y, int color, boolean shadow) {
        if (text == null || text.isEmpty() || (color >>> 24) == 0) {
            return x;
        }

        for (int i = 0; i < text.length(); i++) {
            glyph(text.charAt(i));
        }
        flush();

        int originalColor = color;
        int currentColor = color;
        if (shadow) {
            int a = (originalColor >>> 24) & 0xFF;
            int rgb = (originalColor & 0xFCFCFC) >> 2;
            currentColor = (a << 24) | rgb;
        }

        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(this.scale, this.scale);

        float curX = 0.0f;
        float curY = this.baselineOffset;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\u00A7' && i + 1 < text.length()) {
                char code = text.charAt(i + 1);
                currentColor = getCodeColor(code, originalColor, shadow);
                i++;
                continue;
            }
            if (c == '\n') {
                curX = 0.0f;
                curY += this.lineHeight;
                continue;
            }

            Glyph glyph = this.glyphs.get(c);
            if (glyph == null) {
                glyph = glyph(c);
            }
            if (glyph != null) {
                if (glyph.w() > 0 && glyph.h() > 0) {
                    graphics.blit(RenderPipelines.GUI_TEXTURED, this.textureId,
                            Math.round(curX + glyph.offX()), Math.round(curY + glyph.offY()),
                            glyph.u(), glyph.v(), glyph.w(), glyph.h(), ATLAS, ATLAS, currentColor);
                }
                curX += glyph.advance();
            }
        }

        graphics.pose().popMatrix();
        return x + curX * this.scale;
    }

    private float draw(GuiGraphics graphics, Component component, float x, float y, int color, boolean shadow) {
        if (component == null || (color >>> 24) == 0) {
            return x;
        }

        FormattedCharSequence sequence = component.getVisualOrderText();
        sequence.accept((idx, style, codePoint) -> {
            glyph((char) codePoint);
            return true;
        });
        flush();

        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(this.scale, this.scale);

        float[] cur = new float[]{0.0f, this.baselineOffset};

        sequence.accept((idx, style, codePoint) -> {
            if (codePoint == '\n') {
                cur[0] = 0.0f;
                cur[1] += this.lineHeight;
                return true;
            }

            int charColor = color;
            if (style != null && style.getColor() != null) {
                int rgb = style.getColor().getValue();
                int a = (color >>> 24) & 0xFF;
                if (shadow) {
                    rgb = (rgb & 0xFCFCFC) >> 2;
                }
                charColor = (a << 24) | (rgb & 0x00FFFFFF);
            } else if (shadow) {
                int a = (color >>> 24) & 0xFF;
                int rgb = (color & 0xFCFCFC) >> 2;
                charColor = (a << 24) | rgb;
            }

            Glyph glyph = glyph((char) codePoint);
            if (glyph != null) {
                if (glyph.w() > 0 && glyph.h() > 0) {
                    graphics.blit(RenderPipelines.GUI_TEXTURED, this.textureId,
                            Math.round(cur[0] + glyph.offX()), Math.round(cur[1] + glyph.offY()),
                            glyph.u(), glyph.v(), glyph.w(), glyph.h(), ATLAS, ATLAS, charColor);
                }
                cur[0] += glyph.advance();
            }
            return true;
        });

        graphics.pose().popMatrix();
        return x + cur[0] * this.scale;
    }

    public float getStringWidth(String text) {
        if (text == null || text.isEmpty()) {
            return 0.0f;
        }
        float w = 0.0f;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\u00A7' && i + 1 < text.length()) {
                i++;
                continue;
            }
            Glyph glyph = glyph(c);
            if (glyph != null) {
                w += glyph.advance();
            }
        }
        return w * this.scale;
    }

    public float getStringWidth(Component component) {
        if (component == null) {
            return 0.0f;
        }
        float[] w = new float[]{0.0f};
        component.getVisualOrderText().accept((idx, style, codePoint) -> {
            Glyph glyph = glyph((char) codePoint);
            if (glyph != null) {
                w[0] += glyph.advance();
            }
            return true;
        });
        return w[0] * this.scale;
    }

    public int getFontHeight() {
        return Math.round(this.lineHeight * this.scale);
    }

    private int getCodeColor(char c, int originalColor, boolean shadow) {
        int index = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(c));
        if (index < 0 || index == 21 || index >= 16) {
            if (shadow) {
                int a = (originalColor >>> 24) & 0xFF;
                int rgb = (originalColor & 0xFCFCFC) >> 2;
                return (a << 24) | rgb;
            }
            return originalColor;
        }
        int rgb = COLOR_CODES[index];
        int a = (originalColor >>> 24) & 0xFF;
        if (shadow) {
            rgb = (rgb & 0xFCFCFC) >> 2;
        }
        return (a << 24) | rgb;
    }

    private record Glyph(int u, int v, int w, int h, int offX, int offY, float advance) {
    }
}
