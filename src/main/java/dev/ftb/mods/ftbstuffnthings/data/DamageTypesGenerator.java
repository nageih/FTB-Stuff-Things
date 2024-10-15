package dev.ftb.mods.ftbstuffnthings.data;

import dev.ftb.mods.ftbstuffnthings.registry.ModDamageSources;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypesGenerator  {
    public static void bootstrap(BootstrapContext<DamageType> ctx) {
        ctx.register(ModDamageSources.STATIC_ELECTRIC, new DamageType("static_electric", 0.0F));
    }
}
