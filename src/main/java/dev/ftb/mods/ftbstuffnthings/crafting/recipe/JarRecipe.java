package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftblibrary.util.NetworkHelper;
import dev.ftb.mods.ftbstuffnthings.crafting.NoInventory;
import dev.ftb.mods.ftbstuffnthings.integration.stages.StageHelper;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class JarRecipe implements Recipe<NoInventory>, Comparable<JarRecipe> {
	private final Temperature temperature;
	private final int time;
	private final List<SizedIngredient> inputItems;
	private final List<SizedFluidIngredient> inputFluids;
	private final List<ItemStack> outputItems;
	private final List<FluidStack> outputFluids;
	private final boolean canRepeat;
	private final String stage;
	private final Lazy<String> filterText = Lazy.of(this::buildFilterText);

	public JarRecipe(List<SizedIngredient> inputItems, List<SizedFluidIngredient> inputFluids,
					 List<ItemStack> outputItems, List<FluidStack> outputFluids,
					 Temperature temperature, int time, boolean canRepeat, String stage)
	{
		this.inputItems = inputItems;
		this.inputFluids = inputFluids;
		this.outputItems = outputItems;
		this.outputFluids = outputFluids;
		this.temperature = temperature;
		this.time = time;
		this.canRepeat = canRepeat;
		this.stage = stage;
	}

	@Override
	public boolean matches(NoInventory inv, Level world) {
		return true;
	}

	@Override
	public ItemStack assemble(NoInventory noInventory, HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipesRegistry.TEMPERED_JAR_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return RecipesRegistry.TEMPERED_JAR_TYPE.get();
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public int getTime() {
		return time;
	}

	public List<SizedIngredient> getInputItems() {
		return inputItems;
	}

	public List<SizedFluidIngredient> getInputFluids() {
		return inputFluids;
	}

	public List<ItemStack> getOutputItems() {
		return outputItems;
	}

	public List<FluidStack> getOutputFluids() {
		return outputFluids;
	}

	public boolean canRepeat() {
		return canRepeat;
	}

	public String getStage() {
		return stage;
	}

	public boolean isAvailableFor(Player player) {
		return stage.isEmpty() || StageHelper.hasStage(player, stage);
	}

	public boolean hasItems() {
		return !inputItems.isEmpty() || !outputItems.isEmpty();
	}

	public boolean hasFluids() {
		return !inputFluids.isEmpty() || !outputFluids.isEmpty();
	}

	private int getTempOrder() {
		return temperature.ordinal();
	}

	public String getFilterText() {
		return filterText.get();
	}

	private String buildFilterText() {
		LinkedHashSet<String> set = new LinkedHashSet<>();

		for (ItemStack stack : outputItems) {
			set.add(stack.getHoverName().getString().trim().toLowerCase());
		}

		for (FluidStack stack : outputFluids) {
			set.add(stack.getHoverName().getString().trim().toLowerCase());
		}

		for (SizedIngredient ingredient : inputItems) {
			for (ItemStack stack : ingredient.ingredient().getItems()) {
				set.add(stack.getHoverName().getString().trim().toLowerCase());
			}
		}

		for (SizedFluidIngredient ingredient : inputFluids) {
			for (FluidStack stack : ingredient.ingredient().getStacks()) {
				set.add(stack.getHoverName().getString().trim().toLowerCase());
			}
		}

		return String.join(" ", set);
	}

	public List<Either<SizedFluidIngredient,SizedIngredient>> allInputs() {
		List<Either<SizedFluidIngredient,SizedIngredient>> res = new ArrayList<>();
		inputFluids.forEach(f -> res.add(Either.left(f)));
		inputItems.forEach(i -> res.add(Either.right(i)));
		return res;
	}

	/**
	 * Test if the given item and fluids match this recipe, optionally taking item/fluid amounts into consideration.
	 *
	 * @param jarTemperature	the current jar temperature
	 * @param jarItems			the items to test
	 * @param jarFluids			the fluids to test
	 * @param checkAmounts		true to check ingredient amounts too, false to just check for the right items/fluids
	 * @return true if the recipe matches, false otherwise
	 */
	public boolean test(Temperature jarTemperature, IItemHandler jarItems, IFluidHandler jarFluids, boolean checkAmounts) {
		if (jarTemperature != getTemperature()) {
			return false;
		}

		int matched = 0;
		for (SizedIngredient inputItem : inputItems) {
			for (int i = 0; i < jarItems.getSlots(); i++) {
				ItemStack toTest = jarItems.getStackInSlot(i);
				if (checkAmounts ? inputItem.test(toTest) : inputItem.ingredient().test(toTest)) {
					matched++;
					break;
				}
			}
		}
		if (matched != inputItems.size()) return false;

		matched = 0;
		for (SizedFluidIngredient inputFluid : inputFluids) {
			for (int i = 0; i < jarFluids.getTanks(); i++) {
				FluidStack toTest = jarFluids.getFluidInTank(i);
				if (checkAmounts ? inputFluid.test(toTest) : inputFluid.ingredient().test(toTest)) {
					matched++;
					break;
				}
			}
		}
		return matched == inputFluids.size();
	}

	public int inputIngredientCount() {
		return inputFluids.size() + inputItems.size();
	}

	@Override
	public int compareTo(@NotNull JarRecipe o) {
		// compare by temperature, then by number of input ingredients, then by total item count, then by total fluid count
		// the largest number and/or count of ingredients sorts first

		int c = getTemperature().compareTo(o.getTemperature());
		if (c != 0) return c;

		c = Integer.compare(o.inputIngredientCount(), inputIngredientCount());
		if (c != 0) return c;

		c = Integer.compare(
				o.getInputItems().stream().mapToInt(SizedIngredient::count).sum(),
				getInputItems().stream().mapToInt(SizedIngredient::count).sum()
		);
		if (c != 0) return c;

		return Integer.compare(
				o.getInputFluids().stream().mapToInt(SizedFluidIngredient::amount).sum(),
				getInputFluids().stream().mapToInt(SizedFluidIngredient::amount).sum()
		);
	}

	public interface IFactory<T extends JarRecipe> {
		T create(List<SizedIngredient> inputItems, List<SizedFluidIngredient> inputFluids, List<ItemStack> outputItems, List<FluidStack> outputFluids, Temperature temperature, int time, boolean canRepeat, String stage);
	}

	public static class Serializer<T extends JarRecipe> implements RecipeSerializer<T> {
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf,T> streamCodec;

		public Serializer(IFactory<T> factory) {
			codec = RecordCodecBuilder.<T>mapCodec(builder -> builder.group(
							SizedIngredient.FLAT_CODEC.listOf(0, 3).optionalFieldOf("input_items", List.of())
									.forGetter(JarRecipe::getInputItems),
							SizedFluidIngredient.FLAT_CODEC.listOf(0, 3).optionalFieldOf("input_fluids", List.of())
									.forGetter(JarRecipe::getInputFluids),
							ItemStack.CODEC.listOf(0, 3).optionalFieldOf("output_items", List.of())
									.forGetter(JarRecipe::getOutputItems),
							FluidStack.CODEC.listOf(0, 3).optionalFieldOf("output_fluids", List.of())
									.forGetter(JarRecipe::getOutputFluids),
							StringRepresentable.fromEnum(Temperature::values).optionalFieldOf("temperature", Temperature.NORMAL)
									.forGetter(JarRecipe::getTemperature),
							ExtraCodecs.POSITIVE_INT.optionalFieldOf("time", 200)
									.forGetter(JarRecipe::getTime),
							Codec.BOOL.optionalFieldOf("can_repeat", true)
									.forGetter(JarRecipe::canRepeat),
							Codec.STRING.optionalFieldOf("stage", "")
									.forGetter(JarRecipe::getStage)
					).apply(builder, factory::create))
					.validate(Serializer::validateRecipe);

			streamCodec = NetworkHelper.composite(
					SizedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), JarRecipe::getInputItems,
					SizedFluidIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), JarRecipe::getInputFluids,
					ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), JarRecipe::getOutputItems,
					FluidStack.STREAM_CODEC.apply(ByteBufCodecs.list()), JarRecipe::getOutputFluids,
					NeoForgeStreamCodecs.enumCodec(Temperature.class), JarRecipe::getTemperature,
					ByteBufCodecs.VAR_INT, JarRecipe::getTime,
					ByteBufCodecs.BOOL, JarRecipe::canRepeat,
					ByteBufCodecs.STRING_UTF8, JarRecipe::getStage,
					factory::create
			);
		}

		private static <T extends JarRecipe> @NotNull DataResult<T> validateRecipe(T recipe) {
			if (recipe.getInputItems().isEmpty() && recipe.getInputFluids().isEmpty()) {
				return DataResult.error(() -> "at least one of input_items & input_fluids must be non-empty!");
			}
			if (recipe.getOutputItems().isEmpty() && recipe.getOutputFluids().isEmpty()) {
				return DataResult.error(() -> "at least one of output_items & output_fluids must be non-empty!");
			}
			if (recipe.inputIngredientCount() > 3) {
				return DataResult.error(() -> "must be 1-3 item & fluid inputs combined!");
			}
			if (recipe.getOutputItems().size() + recipe.getOutputFluids().size() > 3) {
				return DataResult.error(() -> "must be 1-3 item & fluid outputs combined!");
			}
			return DataResult.success(recipe);
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