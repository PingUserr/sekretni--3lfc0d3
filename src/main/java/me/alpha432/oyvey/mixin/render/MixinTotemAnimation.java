package me.alpha432.oyvey.mixin.render;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.render.NoRenderModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class MixinTotemAnimation {

    @Inject(method = "renderItemActivationAnimation", at = @At("HEAD"), cancellable = true)
    private void renderItemActivationAnimation(PoseStack poseStack, float partialTick, SubmitNodeCollector nodeCollector, CallbackInfo ci) {
        NoRenderModule noRender = OyVey.moduleManager.getModuleByClass(NoRenderModule.class);

        if (noRender != null && noRender.isEnabled() && noRender.TotemAnimation.getValue()) {
            ci.cancel();
        }
    }
}