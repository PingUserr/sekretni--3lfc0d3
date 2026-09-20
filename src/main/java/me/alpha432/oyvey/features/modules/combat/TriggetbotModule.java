package me.alpha432.oyvey.features.modules.combat;

import me.alpha432.oyvey.event.impl.render.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

public class TriggetbotModule extends Module {

    public final Setting<Boolean> Players = bool("Players", false);
    public final Setting<Boolean> Mobs = bool("Mobs", false);
    public final Setting<Boolean> Eat = bool("Eat", true);
    public final Setting<Boolean> OnlyCrit = bool("OnlyCrit", false);
    public final Setting<Boolean> SmartOnlyCrit = bool("SmartOnlyCrit", false);

    private int attackTimer;

    public TriggetbotModule() {
        super("TriggetBot", "Автоматически бьет при наводке на хитбокс игрока", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        if (attackTimer > 0) {
            attackTimer--;
            return;
        }

        if (Eat.getValue() && mc.player.isUsingItem()) {
            return;
        }

        if (mc.hitResult instanceof EntityHitResult entityHit) {
            if (entityHit.getEntity() instanceof LivingEntity target) {

                if (!target.isAlive()) return;

                if (target instanceof Player) {
                    if (!Players.getValue()) return;
                } else if (target instanceof Mob) {
                    if (!Mobs.getValue()) return;
                } else {
                    return;
                }

                if (!canAttack(mc.player)) return;

                if (mc.gameMode == null) return;

                mc.gameMode.attack(mc.player, target);
                mc.player.swing(InteractionHand.MAIN_HAND);

                mc.gameMode.attack(mc.player, target);
                mc.player.swing(InteractionHand.MAIN_HAND);
                attackTimer = 10;
            }
        }
    }

    private boolean canAttack(LocalPlayer player) {
        float attackStrength = player.getAttackStrengthScale(0.5F);

        if (attackStrength < 0.9F) {
            return false;
        }

        if (SmartOnlyCrit.getValue()) {
            return canSmartCrit(player);
        }

        if (OnlyCrit.getValue()) {
            return !player.onGround() && player.fallDistance > 0.0F;
        }

        return true;
    }

    private boolean canSmartCrit(LocalPlayer player) {
        if (player.onGround()) {
            return true;
        }

        return player.fallDistance > 0.0F;
    }
}