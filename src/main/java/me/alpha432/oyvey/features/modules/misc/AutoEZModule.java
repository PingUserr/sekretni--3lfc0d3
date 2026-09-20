package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.world.entity.player.Player;

public class AutoEZModule extends Module {

    public final Setting<String> Message = str("Message", "1");

    private Player target;

    public AutoEZModule() {
        super("AutoEZ", "Автоматически пишет сообщение в чат", Category.MISC);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (target != null && !target.isAlive()) {
            sendMessage();
            target = null;
        }
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    private void sendMessage() {
        if (mc.player == null) return;

        mc.player.connection.sendChat(Message.getValue());
    }

    @Override
    public void onDisable() {
        target = null;
    }
}