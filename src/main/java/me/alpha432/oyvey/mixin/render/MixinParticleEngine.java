package me.alpha432.oyvey.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.NoRenderModule;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.ParticlesRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {

    @Inject(method = "extract", at = @At("HEAD"), cancellable = true)
    private void extract(ParticlesRenderState renderState, Frustum frustum, Camera camera, float tickProgress, CallbackInfo ci) {
        NoRenderModule noRender = OyVey.moduleManager.getModuleByClass(NoRenderModule.class);

        if (noRender != null && noRender.isEnabled() && noRender.Particles.getValue()) {
            ci.cancel();
        }
    }
}