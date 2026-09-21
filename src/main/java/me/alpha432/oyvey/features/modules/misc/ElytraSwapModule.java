package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElytraSwapModule extends Module {

    private int phase = 0;
    private int elytraSlot = -1;

    public ElytraSwapModule() {
        super("ElytraSwap", "По бинду свапает элитру", Category.MISC);
    }
    @Override
    public void onEnable() {
        if (nullCheck()) {
            this.disable();
            return;
        }

        ItemStack chest = mc.player.getItemBySlot(EquipmentSlot.CHEST);

        if (isChestplate(chest)) {
            elytraSlot = findItem(Items.ELYTRA);
        } else if (chest.is(Items.ELYTRA)) {
            elytraSlot = findChestplate();
        } else {
            this.disable();
            return;
        }

        if (elytraSlot == -1) {
            this.disable();
            return;
        }
        elytraSlot = MenuSlot(elytraSlot);
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
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, elytraSlot, 0, ClickType.PICKUP, mc.player);
            phase = 3;
            return;
        }

        if (phase == 3) {
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, 6,0, ClickType.PICKUP, mc.player);
            phase = 4;
            return;
        }

        if (phase == 4) {
            mc.gameMode.handleInventoryMouseClick(mc.player.containerMenu.containerId, elytraSlot, 0, ClickType.PICKUP, mc.player);
            phase = 5;
            return;
        }

        if (phase == 5) {
            mc.player.closeContainer();
            mc.setScreen(null);
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
    private boolean isChestplate(ItemStack stack) {
        return stack.is(Items.LEATHER_CHESTPLATE)
                || stack.is(Items.CHAINMAIL_CHESTPLATE)
                || stack.is(Items.IRON_CHESTPLATE)
                || stack.is(Items.GOLDEN_CHESTPLATE)
                || stack.is(Items.DIAMOND_CHESTPLATE)
                || stack.is(Items.NETHERITE_CHESTPLATE);
    }
    private int findChestplate() {
        for (int i = 35; i >= 0; i--) {
            ItemStack stack = mc.player.getInventory().getItem(i);

            if (isChestplate(stack)) {
                return i;
            }
        }

        return -1;
    }
}
