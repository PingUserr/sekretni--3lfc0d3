package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.Items;

public class FastUse extends Module {
    public FastUse() {
        super("FastUse", "Без задержки использует опыт", Category.PLAYER);
    }
    @Override
    public void onTick() {
        if (nullCheck());

        if (mc.player.isHolding(Items.EXPERIENCE_BOTTLE)) {
                mc.rightClickDelay = 0;
        }
    }
}
