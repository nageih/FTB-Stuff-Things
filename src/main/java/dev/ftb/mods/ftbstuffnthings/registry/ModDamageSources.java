package dev.ftb.mods.ftbstuffnthings.registry;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSources {
    public static final ResourceKey<DamageType> STATIC_ELECTRIC = ResourceKey.create(Registries.DAMAGE_TYPE, FTBStuffNThings.id("static_electric"));
}
