package me.alpha432.oyvey.mixin.render;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.NoRenderModule;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class MixinLavaFogEnvironment {

    @Inject(method = "getFogType", at = @At("HEAD"), cancellable = true)
    private void getCameraSubmersionType(Camera camera, CallbackInfoReturnable<FogType> cir) {
        NoRenderModule noRender = OyVey.moduleManager.getModuleByClass(NoRenderModule.class);

        if (noRender != null && noRender.isEnabled() && noRender.LavaFog.getValue()) {
            if (camera.getFluidInCamera() == FogType.LAVA) {
                cir.setReturnValue(FogType.ATMOSPHERIC);
            }
        }
    }
}