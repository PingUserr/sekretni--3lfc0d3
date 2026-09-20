package me.alpha432.oyvey.mixin.render;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.NoRenderModule;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class MixinRainSound {

    @Inject(
            method = "tickRainParticles",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
            ),
            cancellable = true
    )
    private void tickRainParticles(CallbackInfo ci) {

        NoRenderModule noRender =
                OyVey.moduleManager.getModuleByClass(NoRenderModule.class);

        if (noRender != null && noRender.isEnabled() && noRender.Rain.getValue()) {
            ci.cancel();
        }
    }
}