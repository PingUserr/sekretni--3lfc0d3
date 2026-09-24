package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.inventory.InventoryUtil;
import me.alpha432.oyvey.util.inventory.Result;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import static me.alpha432.oyvey.util.inventory.InventoryUtil.FULL_SCOPE;
import static me.alpha432.oyvey.util.inventory.InventoryUtil.HOTBAR_SCOPE;

public class SpearSwapModule extends Module {

    private final Setting<Boolean> inventory = bool("Inventory", false);

    public SpearSwapModule() {
        super("SpearSwap", "Свапает на копье и атакует", Category.MISC);
    }

    @Override
    public void onEnable() {
        if (nullCheck()) {
            disable();
            return;
        }

        Result result = InventoryUtil.find(
                stack -> stack.is(ItemTags.SPEARS),
                inventory.getValue() ? FULL_SCOPE : HOTBAR_SCOPE
        );

        if (!result.found()) {
            disable();
            return;
        }

        int oldSlot = mc.player.getInventory().getSelectedSlot();

        if (result.type().name().equals("HOTBAR")) {
            int spearSlot = result.slot();

            if (spearSlot != oldSlot) {
                mc.player.getInventory().setSelectedSlot(spearSlot);
                mc.gameMode.ensureHasSentCarriedItem();
            }

            attack();

            mc.player.getInventory().setSelectedSlot(oldSlot);
            mc.gameMode.ensureHasSentCarriedItem();
        } else {
            int inventorySlot = result.slot();

            int menuSlot = inventorySlot >= 9
                    ? inventorySlot
                    : 36 + inventorySlot;

            mc.gameMode.handleInventoryMouseClick(
                    mc.player.containerMenu.containerId,
                    menuSlot,
                    oldSlot,
                    ClickType.SWAP,
                    mc.player
            );

            attack();

            mc.gameMode.handleInventoryMouseClick(
                    mc.player.containerMenu.containerId,
                    menuSlot,
                    oldSlot,
                    ClickType.SWAP,
                    mc.player
            );
        }

        disable();
    }

    private void attack() {
        ItemStack heldItem = mc.player.getItemInHand(InteractionHand.MAIN_HAND);

        var piercingWeapon = heldItem.get(DataComponents.PIERCING_WEAPON);

        if (piercingWeapon != null) {
            mc.gameMode.piercingAttack(piercingWeapon);
        }
    }
}