package dev.ftb.mods.ftbobb.recipes;

import com.mojang.serialization.MapCodec;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.conditions.ICondition;

public enum DevEnvironmentCondition implements ICondition {
    INSTANCE;

    public static final MapCodec<DevEnvironmentCondition> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public boolean test(IContext context) {
        return !FMLLoader.isProduction();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
