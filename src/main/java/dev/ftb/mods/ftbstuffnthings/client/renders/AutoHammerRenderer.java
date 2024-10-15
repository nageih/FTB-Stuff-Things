package dev.ftb.mods.ftbstuffnthings.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import dev.ftb.mods.ftbstuffnthings.blocks.hammer.AutoHammerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class AutoHammerRenderer implements BlockEntityRenderer<AutoHammerBlockEntity> {
    public AutoHammerRenderer(BlockEntityRendererProvider.Context ignored) {
        super();
    }

    @Override
    public void render(AutoHammerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel().isLoaded(blockEntity.getBlockPos())) {
            poseStack.pushPose();
            poseStack.translate(0.375, 0.3125, 0.375);
            renderBlock(poseStack, bufferSource, packedLight, packedOverlay, blockEntity.getProcessingStack(), blockEntity.getDestroyStage());
            poseStack.popPose();
        }
    }

    static void renderBlock(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn, ItemStack stack, int breakProgress) {
        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
            poseStack.scale(0.25f, 0.25f, 0.25f);

            VertexConsumer vertexconsumer = new SheetedDecalTextureGenerator(bufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(breakProgress)), poseStack.last(), 1.0F);
            MultiBufferSource bufferSource1 = type -> {
                VertexConsumer vc = bufferSource.getBuffer(type);
                return type.affectsCrumbling() ? VertexMultiConsumer.create(vertexconsumer, vc) : vc;
            };

            BlockState state = blockItem.getBlock().defaultBlockState();
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource1, combinedLightIn, combinedOverlayIn, ModelData.EMPTY, null);
        }
    }
}
