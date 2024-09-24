package dev.ftb.mods.ftbobb.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.joml.Matrix4f;

public class RenderUtil {
    public static void renderFluid3d(FluidTank tank, MultiBufferSource bufferSource, Matrix4f posMat, int packedLight, int packedOverlay) {
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

        float s0 = 3.2F / 16F;
        float s1 = 1F - s0;

        float y0 = 0.9F / 16F;
        float y1 = (1F + 11F * tank.getFluidAmount() / (float) tank.getCapacity()) / 16F;

        float u0 = sprite.getU(3 / 16F);
        float v0 = sprite.getV0();
        float u1 = sprite.getU(13 / 16F);
        float v1 = sprite.getV(y1);

        float u0top = sprite.getU(3 / 16F);
        float v0top = sprite.getV(3 / 16F);
        float u1top = sprite.getU(13 / 16F);
        float v1top = sprite.getV(13 / 16F);

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
}
