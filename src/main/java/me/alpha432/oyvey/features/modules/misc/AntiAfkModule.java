package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.InteractionHand;

public class AntiAfkModule extends Module {

    private final Setting<Boolean> jump = bool("jump", false);
    private final Setting<Boolean> click = bool("click", false);


    public AntiAfkModule() {
        super("AntiAfk", "Не дает серверу кикнуть вас за афк", Category.MISC);
    }
    @Override
    public void onTick() {
        if (mc.player == null) return; {
            mc.options.keyJump.setDown(jump.getValue());

            if (click.getValue()) {
                mc.player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
