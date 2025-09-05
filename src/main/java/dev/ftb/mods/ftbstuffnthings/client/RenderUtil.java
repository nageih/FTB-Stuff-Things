package dev.ftb.mods.ftbstuffnthings.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.joml.Matrix4f;

public class RenderUtil {

    public static void renderFluid3d(FluidBounds fluidBounds, FluidTank tank, MultiBufferSource bufferSource, Matrix4f posMat, int packedLight, int packedOverlay) {
        if (tank.isEmpty()) {
            return;
        }

        FluidStack fluid = tank.getFluid();

        IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(fluid.getFluidType());
        ResourceLocation texture = renderProps.getStillTexture(fluid);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        VertexConsumer builder = bufferSource.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));

        int color = renderProps.getTintColor(fluid);
        float r = ((color >> 16) & 255) / 255F;
        float g = ((color >> 8) & 255) / 255F;
        float b = (color & 255) / 255F;
        float a = 1F;

        float s0 = fluidBounds.horizontalInset();
        float s1 = 1F - s0;

        float y0 = fluidBounds.minY();
        float y1 = y0 + ((float) tank.getFluidAmount() / tank.getCapacity()) * fluidBounds.yHeight();

        float u0 = sprite.getU(fluidBounds.horizontalInset());
        float v0 = sprite.getV0();
        float u1 = sprite.getU((1f - fluidBounds.horizontalInset()));
        float v1 = sprite.getV(y1);

        float u0top = sprite.getU(fluidBounds.horizontalInset());
        float v0top = sprite.getV(fluidBounds.horizontalInset());
        float u1top = sprite.getU(1f - fluidBounds.horizontalInset());
        float v1top = sprite.getV(1f - fluidBounds.horizontalInset());

        // top
        builder.addVertex(posMat, s0, y1, s0).setColor(r, g, b, a).setUv(u0top, v0top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        builder.addVertex(posMat, s0, y1, s1).setColor(r, g, b, a).setUv(u0top, v1top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        builder.addVertex(posMat, s1, y1, s1).setColor(r, g, b, a).setUv(u1top, v1top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        builder.addVertex(posMat, s1, y1, s0).setColor(r, g, b, a).setUv(u1top, v0top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);

        // down (not needed)
//        builder.addVertex(posMat, s0, y0, s0).setColor(r, g, b, a).setUv(u0top, v0top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
//        builder.addVertex(posMat, s1, y0, s0).setColor(r, g, b, a).setUv(u1top, v0top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
//        builder.addVertex(posMat, s1, y0, s1).setColor(r, g, b, a).setUv(u1top, v1top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
//        builder.addVertex(posMat, s0, y0, s1).setColor(r, g, b, a).setUv(u0top, v1top).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);

        // south
        builder.addVertex(posMat, s0, y1, s1).setColor(r, g, b, a).setUv(u0, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 0F, 1F);
        builder.addVertex(posMat, s0, y0, s1).setColor(r, g, b, a).setUv(u0, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 1F, 0F);
        builder.addVertex(posMat, s1, y0, s1).setColor(r, g, b, a).setUv(u1, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 0F, 1F);
        builder.addVertex(posMat, s1, y1, s1).setColor(r, g, b, a).setUv(u1, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 0F, 1F);

        // north
        builder.addVertex(posMat, s0, y1, s0).setColor(r, g, b, a).setUv(u0, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 0F, -1F);
        builder.addVertex(posMat, s1, y1, s0).setColor(r, g, b, a).setUv(u1, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 0F, -1F);
        builder.addVertex(posMat, s1, y0, s0).setColor(r, g, b, a).setUv(u1, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 0F, -1F);
        builder.addVertex(posMat, s0, y0, s0).setColor(r, g, b, a).setUv(u0, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(0F, 0F, -1F);

        // west
        builder.addVertex(posMat, s0, y1, s0).setColor(r, g, b, a).setUv(u0, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(-1F, 0F, 0F);
        builder.addVertex(posMat, s0, y0, s0).setColor(r, g, b, a).setUv(u0, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(1F, 0F, 0F);
        builder.addVertex(posMat, s0, y0, s1).setColor(r, g, b, a).setUv(u1, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(1F, 0F, 0F);
        builder.addVertex(posMat, s0, y1, s1).setColor(r, g, b, a).setUv(u1, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(1F, 0F, 0F);

        // east
        builder.addVertex(posMat, s1, y1, s0).setColor(r, g, b, a).setUv(u0, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(1F, 0F, 0F);
        builder.addVertex(posMat, s1, y1, s1).setColor(r, g, b, a).setUv(u1, v0).setOverlay(packedOverlay).setLight(packedLight).setNormal(1F, 0F, 0F);
        builder.addVertex(posMat, s1, y0, s1).setColor(r, g, b, a).setUv(u1, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(1F, 0F, 0F);
        builder.addVertex(posMat, s1, y0, s0).setColor(r, g, b, a).setUv(u0, v1).setOverlay(packedOverlay).setLight(packedLight).setNormal(1F, 0F, 0F);
    }

    public static void renderBlock(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn, ItemStack stack, int breakProgress) {
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

    public record FluidBounds(float horizontalInset, float minY, float yHeight) {
    }

}
