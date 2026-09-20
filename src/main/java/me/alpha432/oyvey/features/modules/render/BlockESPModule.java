package me.alpha432.oyvey.features.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class BlockESPModule extends Module {
    public BlockESPModule() {
        super("BlockESP", "Подсвечивает блоки", Category.RENDER);
    }
    @Override
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;

        if (mc.hitResult instanceof BlockHitResult hit) {
            BlockPos pos = hit.getBlockPos();
            Vec3 camera = mc.gameRenderer.getMainCamera().position();
            double x = pos.getX() - camera.x;
            double y = pos.getY() - camera.y;
            double z = pos.getZ() - camera.z;

            PoseStack matrix = event.getMatrix();

            matrix.pushPose();

            matrix.translate(x, y, z);

            VertexConsumer buffer = mc.renderBuffers()
                    .bufferSource()
                    .getBuffer(RenderTypes.lines());

            ShapeRenderer.renderShape(matrix, buffer, Shapes.block(), 0, 0, 0, 0xFFFF0000,1.0f);

            matrix.popPose();

        }
    }
}
