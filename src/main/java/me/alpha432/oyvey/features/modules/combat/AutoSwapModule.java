package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoSwapModule extends Module {
    private int phase = 0;
    private int targetSlot = -1;

    public AutoSwapModule() {
        super("AutoSwap", "Автоматически свапает на нужный предмет", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            this.disable();
            return;
        }

        ItemStack offhand = mc.player.getOffhandItem();
        Item targetItem = offhand.is(Items.TOTEM_OF_UNDYING) ? Items.PLAYER_HEAD : Items.TOTEM_OF_UNDYING;

        int slot = findItem(targetItem);
        if (slot == -1) {
            this.disable();
            return;
        }

        targetSlot = MenuSlot(slot);
        phase = 1;
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (phase == 1) {
            if (mc.screen == null) {
                mc.setScreen(new InventoryScreen(mc.player));
            }
            phase = 2;
            return;
        }

        if (phase == 2) {
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, targetSlot, 40, ClickType.SWAP, mc.player);
            phase = 3;
            return;
        }

        if (phase == 3) {
            mc.player.closeContainer();
            mc.setScreen(null);
            phase = 0;
            targetSlot = -1;
            this.disable();
        }
    }

    private int findItem(Item item) {
        for (int i = 35; i >= 0; i--) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            if (stack.is(item)) {
                return i;
            }
        }
        return -1;
    }

    private int MenuSlot(int invIndex) {
        if (invIndex >= 0 && invIndex <= 8) {
            return invIndex + 36;
        }
        if (invIndex >= 9 && invIndex <= 35) {
            return invIndex;
        }
        return -1;
    }
}