package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;

public class PortalGodModule extends Module {
    public PortalGodModule() {
        super("PortalGod", "Дает открывать гуи в портале", Category.PLAYER);
    }
    @Override
    public void onTick() {
        if (nullCheck()) return;
    }
}

