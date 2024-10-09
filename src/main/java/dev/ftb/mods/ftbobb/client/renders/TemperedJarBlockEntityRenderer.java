package dev.ftb.mods.ftbobb.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ftb.mods.ftbobb.blocks.jar.TemperedJarBlockEntity;
import dev.ftb.mods.ftbobb.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class TemperedJarBlockEntityRenderer implements BlockEntityRenderer<TemperedJarBlockEntity> {
    public TemperedJarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(TemperedJarBlockEntity jar, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        int nTanks = jar.getFluidHandler().getTanks();
        int fluidAmount = 0;
        for (int i = 0; i < nTanks; i++) {
            FluidStack stack = jar.getFluidHandler().getFluidInTank(i);
            if (!stack.isEmpty()) {
                fluidAmount += stack.getAmount();
                FluidTank tank = new FluidTank(TemperedJarBlockEntity.TANK_CAPACITY * nTanks);
                tank.setFluid(stack);
                RenderUtil.renderFluid3d(tank, bufferSource, poseStack.last().pose(), packedLight, packedOverlay);
                double yOff = (double) stack.getAmount() / tank.getCapacity();
                poseStack.translate(0.0, yOff * (11.1F / 16F), 0.0);
            }
        }
        boolean enoughFluidToFloatItems = fluidAmount > 2000;

        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < jar.getInputItemHandler().getSlots(); i++) {
            ItemStack stack = jar.getInputItemHandler().getStackInSlot(i);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        float circleRadius = stacks.size() == 1 ? 0 : 0.17f;
        float degreesPerStack = 360f / stacks.size();
        double ticks = jar.getLevel().getGameTime() + partialTick;
        float yBob = enoughFluidToFloatItems ? Mth.sin(((float) ticks  / 10) % 360) * 0.01f : 0;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.translate(0.5, 0.1, 0.5);
        for (int i = 0; i < stacks.size(); i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(i * degreesPerStack + (enoughFluidToFloatItems ? (float) ticks / 3 % 360 : 0.0f)));
            poseStack.translate(circleRadius, yBob,0);
            poseStack.scale(0.25f, 0.25f, 0.25f);

            BakedModel bakedModel = itemRenderer.getModel(stacks.get(i), jar.getLevel(), null, 0);
            itemRenderer.render(stacks.get(i), ItemDisplayContext.FIXED, true, poseStack, bufferSource, packedLight, packedOverlay, bakedModel);

            poseStack.popPose();
        }
        
        poseStack.popPose();
    }
}
