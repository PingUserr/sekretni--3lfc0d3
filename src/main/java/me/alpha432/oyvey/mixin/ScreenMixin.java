package me.alpha432.oyvey.mixin;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.misc.PortalGodModule;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Inject(method = "isAllowedInPortal", at = @At("HEAD"), cancellable = true)
    private void onIsAllowedInPortal(CallbackInfoReturnable<Boolean> cir) {
        if (OyVey.moduleManager.getModuleByClass(PortalGodModule.class).isEnabled()) {
            cir.setReturnValue(true);
        }
    }
}