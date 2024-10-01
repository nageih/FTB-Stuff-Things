package dev.ftb.mods.ftbobb.blocks;

import dev.ftb.mods.ftbobb.recipes.DripperRecipe;
import dev.ftb.mods.ftbobb.recipes.NoInventory;
import dev.ftb.mods.ftbobb.recipes.RecipeCaches;
import dev.ftb.mods.ftbobb.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbobb.registry.RecipesRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class DripperBlockEntity extends BlockEntity {
	private final FluidTank tank;
	private int prevAmount = -1;
	private Fluid prevFluid = null;
	private RecipeHolder<DripperRecipe> currentRecipe = null;

	public DripperBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntitiesRegistry.DRIPPER.get(), pos, state);

		tank = new FluidTank(4000) {
			@Override
			protected void onContentsChanged() {
				fluidChanged();
			}
		};
	}

	public FluidTank getTank() {
		return tank;
	}

	public void writeData(CompoundTag tag, HolderLookup.Provider provider) {
		tag.put("Tank", tank.writeToNBT(provider, new CompoundTag()));
	}

	public void readData(CompoundTag tag, HolderLookup.Provider provider) {
		tank.readFromNBT(provider, tag.getCompound("Tank"));
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		writeData(tag, provider);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);

		readData(tag, provider);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		// server-side, chunk loading
		return Util.make(new CompoundTag(), tag -> saveAdditional(tag, provider));
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	private void fluidChanged() {
		setChanged();

//		if (!level.isClientSide() && prevAmount != tank.getFluidAmount()) {
//			prevAmount = tank.getFluidAmount();
//			level.setBlock(worldPosition, getBlockState().setValue(DripperBlock.ACTIVE, prevAmount > 0), Block.UPDATE_ALL);
//		}

		if (!level.isClientSide() && prevFluid != tank.getFluid().getFluid()) {
			prevFluid = tank.getFluid().getFluid();
			// sync contained fluid to client, so it knows what sort of drip particle to play
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
		}
	}

	public void serverTick(ServerLevel serverLevel) {
		if (!tank.isEmpty() && serverLevel.getGameTime() % 20 == 0) {
			currentRecipe = RecipeCaches.DRIPPER.getCachedRecipe(this::searchForRecipe, this::genRecipeHash).orElse(null);

			level.setBlock(worldPosition, getBlockState().setValue(DripperBlock.ACTIVE, !tank.getFluid().isEmpty() && currentRecipe != null), Block.UPDATE_ALL);

			if (currentRecipe != null) {
				DripperRecipe recipe = currentRecipe.value();
				boolean success = false;
				if (tank.getFluidAmount() >= recipe.getFluid().getAmount()) {
					if (serverLevel.random.nextDouble() < recipe.getChance()) {
						level.setBlock(getBlockPos().below(), recipe.getOutputState(), Block.UPDATE_ALL);
						success = true;
					}
					if (success || recipe.consumeFluidOnFail()) {
						tank.drain(recipe.getFluid().getAmount(), IFluidHandler.FluidAction.EXECUTE);
					}
				}
			}
		}
	}

	private int genRecipeHash() {
		int fluidHash = FluidStack.hashFluidAndComponents(tank.getFluid());
		BlockState blockBelow = getLevel().getBlockState(getBlockPos().below());

		return Objects.hash(fluidHash, blockBelow);
	}

	private Optional<RecipeHolder<DripperRecipe>> searchForRecipe() {
		BlockState blockBelow = getLevel().getBlockState(getBlockPos().below());

		return level.getRecipeManager().getRecipesFor(RecipesRegistry.DRIP_TYPE.get(), NoInventory.INSTANCE, level).stream()
				.filter(r -> r.value().testInput(tank.getFluid(), blockBelow))
				.findFirst();
	}
}