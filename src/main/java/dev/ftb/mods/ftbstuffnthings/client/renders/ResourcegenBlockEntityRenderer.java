package dev.ftb.mods.ftbstuffnthings.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbstuffnthings.blocks.cobblegen.BaseResourceGenBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ResourcegenBlockEntityRenderer implements BlockEntityRenderer<BaseResourceGenBlockEntity> {
    public static final float TEX_ANIM = 10f;
    public static final float SPEED_FACTOR = 3f;  // Increase this value to slow down the animation
    public static final int TEXTURE_OFFSET = 2;   // Adjust as needed

    public ResourcegenBlockEntityRenderer(BlockEntityRendererProvider.Context ignoredContext) {
    }

    @Override
    public void render(BaseResourceGenBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel() != null
                && blockEntity.getLevel().isLoaded(blockEntity.getBlockPos())
                && blockEntity.getBlockState().getValue(BlockStateProperties.ENABLED))
        {
            var tick = blockEntity.getLevel().getGameTime();
            int time = (int) ((tick / SPEED_FACTOR) % TEX_ANIM);
            time = (time + TEXTURE_OFFSET) % (int) TEX_ANIM;

            poseStack.pushPose();
            poseStack.translate(0.375, 0.125, 0.375);
            RenderUtil.renderBlock(poseStack, bufferSource, packedLight, packedOverlay, blockEntity.generatedItem().getDefaultInstance(), time);
            poseStack.popPose();
        }
    }
}
