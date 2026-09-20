package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.event.impl.render.Render3DEvent;
import me.alpha432.oyvey.event.system.Subscribe;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.awt.*;

public class ESPModule extends Module {

    public Setting<Boolean> box = bool("Box", true);
    public Setting<Boolean> fill = bool("Fill", true);
    public Setting<Color> boxColor = color("BoxColor", 255, 0, 0, 255);
    public Setting<Color> fillColor = color("FillColor", 255, 0, 0, 50);
    public Setting<Float> lineWidth = num("LineWidth", 1.5f, 0.1f, 5.0f);

    public ESPModule() {
        super("ESP", "Подсвечивает игроков сквозь стены", Category.RENDER);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (nullCheck()) return;

        for (Player player : mc.level.players()) {
            if (player == mc.player || !player.isAlive()) continue;

            double dx = Mth.lerp(event.getDelta(), player.xOld, player.getX()) - player.getX();
            double dy = Mth.lerp(event.getDelta(), player.yOld, player.getY()) - player.getY();
            double dz = Mth.lerp(event.getDelta(), player.zOld, player.getZ()) - player.getZ();

            AABB aabb = player.getBoundingBox().move(dx, dy, dz);

            if (fill.getValue()) {
                RenderUtil.drawBoxFilled(event.getMatrix(), aabb, fillColor.getValue());
            }

            if (box.getValue()) {
                RenderUtil.drawBox(event.getMatrix(), aabb, boxColor.getValue(), lineWidth.getValue());
            }
        }
    }
}