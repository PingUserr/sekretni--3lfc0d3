package me.alpha432.oyvey.mixin.render;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.NoRenderModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WeatherEffectRenderer.class)
public class MixinRainParticles {

    @Redirect(
            method = "tickRainParticles",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
            )
    )
    private void addParticle(ClientLevel level,
                             ParticleOptions particleOptions,
                             double x, double y, double z,
                             double dx, double dy, double dz) {

        NoRenderModule noRender =
                OyVey.moduleManager.getModuleByClass(NoRenderModule.class);

        if (noRender != null && noRender.isEnabled() && noRender.Rain.getValue()) {
            return;
        }

        level.addParticle(particleOptions, x, y, z, dx, dy, dz);
    }
}