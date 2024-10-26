package dev.ftb.mods.ftbstuffnthings.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.Axis;
import dev.ftb.mods.ftbstuffnthings.blocks.cobblegen.CobblegenBlockEntity;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceBlock;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class CobblegenBlockEntityRenderer implements BlockEntityRenderer<CobblegenBlockEntity> {
    public CobblegenBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CobblegenBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        var tick = blockEntity.getLevel().getGameTime();

        float texAnim = 10f;
        float speedFactor = 3f; // Increase this value to slow down the animation
        int time = (int) ((tick / speedFactor) % texAnim);

        int textureOffset = 2; // Adjust the offset as needed
        time = (time + textureOffset) % (int)texAnim;

        if (blockEntity.getLevel().isLoaded(blockEntity.getBlockPos()) && blockEntity.getBlockState().getValue(BlockStateProperties.ENABLED)) {
            poseStack.pushPose();
            poseStack.translate(0.375, 0.125, 0.375);
            RenderUtil.renderBlock(poseStack, bufferSource, packedLight, packedOverlay, Items.COBBLESTONE.getDefaultInstance(), time);
            poseStack.popPose();
        }

    }

}
