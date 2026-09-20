package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AutoToolModule extends Module {

    public AutoToolModule() {
        super("AutoTool", "Автоматически выбирает лучший инструмент при наводке на определенный блок", Category.PLAYER);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (!(mc.hitResult instanceof BlockHitResult hit))
            return;

        BlockPos pos = hit.getBlockPos();
        BlockState block = mc.level.getBlockState(pos);

        int bestSlot = -1;
        float bestSpeed = 1.0f;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);

            float speed = stack.getDestroySpeed(block);

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        if (bestSlot != -1) {
            mc.player.getInventory().setSelectedSlot(bestSlot);
        }
    }
}