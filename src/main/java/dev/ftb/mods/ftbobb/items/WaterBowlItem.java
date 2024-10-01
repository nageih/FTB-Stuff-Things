package dev.ftb.mods.ftbobb.items;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.registry.ComponentsRegistry;
import dev.ftb.mods.ftbobb.registry.ItemsRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class WaterBowlItem extends Item {
	public WaterBowlItem() {
		super(new Properties().stacksTo(1));
	}

	public static boolean fillBowl(Level level, Player player) {
		BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

		if (hit.getType() == HitResult.Type.BLOCK && level.getBlockState(hit.getBlockPos()).getBlock() == Blocks.WATER) {
			player.awardStat(Stats.ITEM_USED.get(Items.BOWL));
			player.playSound(SoundEvents.BUCKET_FILL, 1F, 1F);
			return true;
		}

		return false;
	}

	public static class WaterBowlFluidHandler extends FluidHandlerItemStack.SwapEmpty {
		protected static final int BOWL_CAPACITY = FluidType.BUCKET_VOLUME / 4;

		public WaterBowlFluidHandler(ItemStack container) {
			super(ComponentsRegistry.STORED_FLUID, container, new ItemStack(Items.BOWL), BOWL_CAPACITY);

			setFluid(new FluidStack(Fluids.WATER, BOWL_CAPACITY));
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return false;
		}

		@Override
		public boolean canDrainFluidType(FluidStack fluid) {
			return fluid.getFluid() == Fluids.WATER;
		}
	}

	@EventBusSubscriber(modid = FTBOBB.MODID)
	public static class Listener {
		@SubscribeEvent
		public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
			Player player = event.getEntity();
			if (event.getItemStack().getItem() == Items.BOWL && WaterBowlItem.fillBowl(event.getLevel(), player)) {
				event.getItemStack().shrink(1);

				if (!event.getLevel().isClientSide()) {
					ItemHandlerHelper.giveItemToPlayer(player, ItemsRegistry.WATER_BOWL.toStack(), player.getInventory().selected);
				}

				player.swing(event.getHand());
				event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
				event.setCanceled(true);
			}
		}
	}
}
