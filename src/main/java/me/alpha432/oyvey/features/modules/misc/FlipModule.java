package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;

public class FlipModule extends Module {

    public FlipModule() {
        super("Flip", "ыдоварплоавы", Category.MISC);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        mc.player.setXRot(90.0F);

    }
}
