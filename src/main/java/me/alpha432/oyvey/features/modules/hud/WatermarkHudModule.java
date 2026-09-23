package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.BuildConfig;
import me.alpha432.oyvey.util.TextUtil;
import net.minecraft.client.Minecraft;

public class WatermarkHudModule extends HudModule {
    public Setting<String> text = str("Text", BuildConfig.NAME);
    public Setting<Boolean> fullVersion = new Setting<>("FullVersion", false);
    public Setting<Boolean> fps = bool("Fps", false);
    public Setting<Boolean> ping = bool("Ping", false);

    public WatermarkHudModule() {
        super("Watermark", "Display watermark", 100, 10);

        if (BuildConfig.USING_GIT) {
            register(fullVersion);
        }
    }

    @Override
    public void onLoad() {
        if (text.getValue() == null || text.getValue().equalsIgnoreCase("OyVey") || text.getValue().isEmpty()) {
            text.setValue(BuildConfig.NAME);
        }
    }

    @Override
    protected void render(Render2DEvent e) {
        super.render(e);

        if (text.getValue() == null || text.getValue().equalsIgnoreCase("OyVey") || text.getValue().isEmpty()) {
            text.setValue(BuildConfig.NAME);
        }

        String watermarkString = "{global} %s {} %s";

        if (fps.getValue()) {
            watermarkString += " | FPS: " + Minecraft.getInstance().getFps();
        }

        if (ping.getValue()) {
            int pingValue = 0;

            var playerInfo = mc.player.connection.getPlayerInfo(mc.player.getUUID());
            if (playerInfo != null) {
                pingValue = playerInfo.getLatency();
            }

            watermarkString += " | Ping: " + pingValue + "ms";
        }

        if (fullVersion.getValue() && BuildConfig.USING_GIT) {
            watermarkString += "/" + BuildConfig.BRANCH + "-" + BuildConfig.HASH;
        }

        net.minecraft.network.chat.MutableComponent component = TextUtil.text(watermarkString, text.getValue(), BuildConfig.VERSION, fps, ping);

        OyVey.fontManager.drawString(e.getContext(), component, getX(), getY(), -1);

        setWidth(OyVey.fontManager.getStringWidth(component));
        setHeight(OyVey.fontManager.getFontHeight());
    }
}

