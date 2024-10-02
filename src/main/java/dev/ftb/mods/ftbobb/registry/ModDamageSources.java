package dev.ftb.mods.ftbobb.registry;

import dev.ftb.mods.ftbobb.FTBOBB;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSources {
    public static final ResourceKey<DamageType> STATIC_ELECTRIC = ResourceKey.create(Registries.DAMAGE_TYPE, FTBOBB.id("static_electric"));
}
