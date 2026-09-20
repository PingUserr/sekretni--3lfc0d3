package me.alpha432.oyvey.features.modules.movement;

import me.alpha432.oyvey.features.modules.Module;

public class NoJumpDelayModule extends Module {
    public NoJumpDelayModule() {
        super("NoJumpDelay", "Убирает делей прыжка", Category.MOVEMENT);
    }
    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (mc.player.isJumping()) return;
    }
}
