package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.inventory.InventoryUtil;
import me.alpha432.oyvey.util.inventory.Result;
import net.minecraft.tags.ItemTags;

import static me.alpha432.oyvey.util.inventory.InventoryUtil.FULL_SCOPE;
import static me.alpha432.oyvey.util.inventory.InventoryUtil.HOTBAR_SCOPE;

public class SpearSwapModule extends Module {

    private final Setting<Boolean> inventory = bool("Inventory", false);

    public SpearSwapModule() {
        super("SpearSwap", "Свапает на спир", Category.MISC);
    }

    int lastSlot = -1;
    @Override
    public void onEnable() {
        onDisable();

        if (nullCheck()) return;

        if ( lastSlot != -1 )
        {
            mc.player.getInventory().setSelectedSlot( lastSlot );
            lastSlot = -1;
            return;
        }

        Result result = InventoryUtil.find(stack -> stack.is(ItemTags.SPEARS), inventory.getValue() ? FULL_SCOPE : HOTBAR_SCOPE);

        InventoryUtil.withSwap(result, () -> mc.options.keyAttack.setDown(true));
        lastSlot = result.slot();
    }
}