package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class AutoSprintModule extends Module {
    public AutoSprintModule() {
        super("AutoSprint", "Автоматически бежит", Category.MOVEMENT);
    }
    @Override
    public void onTick() {
        if (mc.player == null) return; {
            mc.options.keySprint.setDown(true);
        }
    }
}
