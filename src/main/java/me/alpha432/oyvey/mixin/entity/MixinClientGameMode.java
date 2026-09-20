package me.alpha432.oyvey.mixin.entity;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.misc.AutoEZModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MixinClientGameMode {

    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Player player, Entity target, CallbackInfo ci) {
        if (!(target instanceof Player targetPlayer)) return;

        AutoEZModule autoEZ = OyVey.moduleManager.getModuleByClass(AutoEZModule.class);

        if (autoEZ != null && autoEZ.isEnabled()) {
            autoEZ.setTarget(targetPlayer);
        }
    }
}