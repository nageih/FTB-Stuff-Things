package dev.ftb.mods.ftbstuffnthings.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;

public class SimpleFallingBlock extends FallingBlock {
    private static final MapCodec<SimpleFallingBlock> CODEC = simpleCodec(SimpleFallingBlock::new);

    public SimpleFallingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }
}
