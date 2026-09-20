package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.world.item.BlockItem;

public class FastPlaceModule extends Module {


    public FastPlaceModule() {
        super("FastPlace", "Ставит блоки без задержки", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (mc.player.getMainHandItem().getItem() instanceof BlockItem)
            mc.rightClickDelay = 0;
        }
    }
