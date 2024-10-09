package dev.ftb.mods.ftbobb.crafting.recipe;

import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbobb.crafting.BaseRecipe;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import dev.ftb.mods.ftbobb.util.MiscUtil;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Map;

public class DripperRecipe extends BaseRecipe<DripperRecipe> {
	private final String inputStateStr;
	private final Block inputBlock;
	private final Map<Property<?>, Comparable<?>> inputProperties;
	private final String outputString;
	private final BlockState outputState;
	private final FluidStack fluid;
	private final double chance;
	private final boolean consumeFluidOnFail;

	public DripperRecipe(String inputStateStr, String outputStateStr, FluidStack fluid, double chance, boolean consumeFluidOnFail) {
		super(RecipesRegistry.DRIP_SERIALIZER, RecipesRegistry.DRIP_TYPE);

		this.inputStateStr = inputStateStr;
		this.outputString = outputStateStr;
		this.fluid = fluid;
		this.chance = chance;
		this.consumeFluidOnFail = consumeFluidOnFail;

		try {
			BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), new StringReader(inputStateStr), false);
			inputBlock = blockResult.blockState().getBlock();
			inputProperties = blockResult.properties();
		} catch (CommandSyntaxException e) {
			throw new JsonSyntaxException(e);
		}

		try {
			BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), new StringReader(outputStateStr), false);
			outputState = blockResult.blockState();
		} catch (CommandSyntaxException e) {
			throw new JsonSyntaxException(e);
		}
	}

	public String getInputStateStr() {
		return inputStateStr;
	}

	public String getOutputStateStr() {
		return outputString;
	}

	public ItemStack getInputItem() {
		return inputBlock.asItem().getDefaultInstance();
	}

	public ItemStack getOutputItem() {
		return outputState.getBlock().asItem().getDefaultInstance();
	}

	public BlockState getOutputState() {
		return outputState;
	}

	public boolean consumeFluidOnFail() {
		return consumeFluidOnFail;
	}

	public FluidStack getFluid() {
		return fluid;
	}

	public double getChance() {
		return chance;
	}

	public boolean testInput(FluidStack fluidInDripper, BlockState belowState) {
		// note: just checking for a fluid match; not checking amount here
		if (!FluidStack.isSameFluidSameComponents(fluidInDripper, this.fluid) || inputBlock == Blocks.AIR || inputBlock != belowState.getBlock()) {
			return false;
		}

		for (Map.Entry<Property<?>, Comparable<?>> entry : inputProperties.entrySet()) {
			if (!belowState.getValue(entry.getKey()).equals(entry.getValue())) {
				return false;
			}
		}

		return true;
	}

	public interface IFactory<T extends DripperRecipe> {
		T create(String inputString, String outputString, FluidStack fluid, double chance, boolean consumeFluidOnFail);
	}

	public static class Serializer<T extends DripperRecipe> implements RecipeSerializer<T> {
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf,T> streamCodec;

		public Serializer(IFactory<T> factory) {
			this.codec = RecordCodecBuilder.<T>mapCodec(builder -> builder.group(
					Codec.STRING.fieldOf("input").forGetter(DripperRecipe::getInputStateStr),
					Codec.STRING.fieldOf("output").forGetter(DripperRecipe::getOutputStateStr),
					FluidStack.CODEC.fieldOf("fluid").forGetter(DripperRecipe::getFluid),
					Codec.DOUBLE.validate(MiscUtil::validateChanceRange).optionalFieldOf("chance", 1.0).forGetter(DripperRecipe::getChance),
					Codec.BOOL.optionalFieldOf("consume_fluid_on_fail", false).forGetter(DripperRecipe::consumeFluidOnFail)
			).apply(builder, factory::create));

			this.streamCodec = StreamCodec.composite(
					ByteBufCodecs.STRING_UTF8, DripperRecipe::getInputStateStr,
					ByteBufCodecs.STRING_UTF8, DripperRecipe::getOutputStateStr,
					FluidStack.STREAM_CODEC, DripperRecipe::getFluid,
					ByteBufCodecs.DOUBLE, DripperRecipe::getChance,
					ByteBufCodecs.BOOL, DripperRecipe::consumeFluidOnFail,
					factory::create
			);
		}

		@Override
		public MapCodec<T> codec() {
			return codec;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
			return streamCodec;
		}
	}
}
