package dev.ftb.mods.ftbobb.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public class CreativeTemperatureSourceBlock extends Block {
	public CreativeTemperatureSourceBlock() {
		super(Properties.of().mapColor(MapColor.METAL).strength(-1.0F, 3600000.0F).sound(SoundType.METAL));
	}
}
