package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class AutoEatModule extends Module {
    public AutoEatModule() {
        super("AutoEat", "Автоматически ест еду", Category.PLAYER);
    }
    @Override
    public void onTick() {
        if (nullCheck()) return; {
            if (mc.player.getFoodData().getFoodLevel() < 4) {
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.player.getInventory().getItem(i);

                    if (stack.get(DataComponents.FOOD) != null) {
                        mc.player.getInventory().setSelectedSlot(i);
                        mc.options.keyUse.setDown(true);
                    }
                }
            }
        }
    }
}
