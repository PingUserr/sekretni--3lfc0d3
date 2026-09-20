package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class NoRenderModule extends Module {

    public final Setting<Boolean> Rain = bool("Rain", false);
    public final Setting<Boolean> Fire = bool("Fire", false);
    public final Setting<Boolean> Explosion = bool("Explosion", false);
    public final Setting<Boolean> Particles = bool("Particles", false);
    public final Setting<Boolean> WaterFog = bool("Water Fog", false);
    public final Setting<Boolean> LavaFog = bool("Lava Fog", false);
    public final Setting<Boolean> TotemAnimation = bool("Totem Animation", false);

    public NoRenderModule() {
        super("NoRender","Убирает ненужные эффекты", Category.RENDER);
    }
    @Override
    public void onTick() {
        if (nullCheck()) return;


    }
}
