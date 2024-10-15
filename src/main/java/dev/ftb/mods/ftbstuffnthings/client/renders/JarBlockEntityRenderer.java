package dev.ftb.mods.ftbstuffnthings.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbstuffnthings.blocks.jar.JarBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class JarBlockEntityRenderer implements BlockEntityRenderer<JarBlockEntity> {
    public JarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    public void render(JarBlockEntity jar, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        RenderUtil.renderFluid3d(jar.getTank(), bufferSource, poseStack.last().pose(), packedLight, packedOverlay);
    }
}
