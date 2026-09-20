package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChestStealerModule extends Module {

    public enum Mode {
        DEFAULT,
        RANDOM
    }

    public Setting<Mode> mode = mode("Mode", Mode.DEFAULT);
    public Setting<Integer> Delay = num("Delay", 2, 0, 20);

    private int timer;

    public ChestStealerModule() {
        super("ChestStealer", "Автоматически стилит ресурсы с сундука", Category.MISC);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (timer < Delay.getValue()) {
            timer++;
            return;
        }
        timer = 0;

        if (!(mc.screen instanceof ContainerScreen screen)) return;

        ChestMenu menu = (ChestMenu) screen.getMenu();
        int chestSlots = menu.getRowCount() * 9;

        if (mode.getValue() == Mode.DEFAULT) {
            for (Slot slot : menu.slots.subList(0, chestSlots)) {
                if (!slot.hasItem()) continue;

                mc.gameMode.handleInventoryMouseClick(menu.containerId, slot.index, 0, ClickType.QUICK_MOVE, mc.player);
                break;
            }
        } else if (mode.getValue() == Mode.RANDOM) {
            List<Slot> validSlots = new ArrayList<>();
            for (Slot slot : menu.slots.subList(0, chestSlots)) {
                if (slot.hasItem()) {
                    validSlots.add(slot);
                }
            }

            if (!validSlots.isEmpty()) {
                Slot randomSlot = validSlots.get(ThreadLocalRandom.current().nextInt(validSlots.size()));
                mc.gameMode.handleInventoryMouseClick(menu.containerId, randomSlot.index, 0, ClickType.QUICK_MOVE, mc.player);
            }
        }
    }
}