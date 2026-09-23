package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;

public class CoordinatesHudModule extends HudModule {

    public Setting<Boolean> nether = bool("Nether", false);

    public CoordinatesHudModule() {
        super("Coordinates", "Display coordinates", 150, 20);
    }

    @Override
    protected void render(Render2DEvent e) {
        super.render(e);

        if (nullCheck()) return;

        int color = nether.getValue() ? 0xFFFF0000 : 0xFFFFFFFF;

        String coordsStr = String.format("X: %d Y: %d Z: %d",
                mc.player.getBlockX(),
                mc.player.getBlockY(),
                mc.player.getBlockZ());

        if (nether.getValue()) {
            int netherX = mc.player.level().dimension().identifier().getPath().equals("the_nether")
                    ? mc.player.getBlockX() * 8
                    : mc.player.getBlockX() / 8;
            int netherZ = mc.player.level().dimension().identifier().getPath().equals("the_nether")
                    ? mc.player.getBlockZ() * 8
                    : mc.player.getBlockZ() / 8;
            coordsStr += String.format(" [%d, %d]", netherX, netherZ);
        }

        OyVey.fontManager.drawString(e.getContext(), coordsStr, getX(), getY(), -1);

        setWidth(OyVey.fontManager.getStringWidth(coordsStr));
        setHeight(OyVey.fontManager.getFontHeight());
    }
}

