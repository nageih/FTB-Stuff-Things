package dev.ftb.mods.ftbobb.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ftb.mods.ftbobb.blocks.PumpBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class PumpBlockEntityRender implements BlockEntityRenderer<PumpBlockEntity> {
    public PumpBlockEntityRender(BlockEntityRenderDispatcher arg) {
        super();
    }

    @Override
    public void render(PumpBlockEntity pump, float f, PoseStack stack, MultiBufferSource renderer, int light, int otherlight) {
        if (Minecraft.getInstance().player == null) {
            return;
        }

        Vec3 cameraPos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
        BlockPos blockPos = pump.getBlockPos();
        double distance = cameraPos.distanceToSqr(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));

        if (distance > 30) {
            return;
        }

        double v = Mth.atan2(cameraPos.z() - (blockPos.getZ() + .5F), cameraPos.x() - (blockPos.getX() + .5F));
        stack.pushPose();
        stack.translate(.5F, 1.4F, .5F);
        stack.scale(.020F, -.020F,.020F);
        stack.mulPose(Axis.YP.rotation((float) ((Math.PI / 2) - (float) v)));

//        Screen.drawCenteredString(stack, Minecraft.getInstance().font, pump.creative ? "∞" : "Time left", 0, (pump.creativeItem != null && pump.creative) ? -10 : -5, 0xFFFFFFFF);
//        ResourceLocation registryName = pump.creativeFluid.getRegistryName();
//        if (pump.creative && registryName != null) {
//            Screen.drawCenteredString(stack, Minecraft.getInstance().font, ForgeI18n.parseMessage("fluid." + (registryName.getNamespace().equals("minecraft") ? "ftbsluice" : registryName.getNamespace()) + "." + registryName.getPath()), 0, 0, 0xFFFFFFFF);
//            Screen.drawCenteredString(stack, Minecraft.getInstance().font, pump.creativeItem != null ? pump.creativeItem.getDescription() : new TextComponent("Not item"), 0, 10, 0xFFFFFFFF);
//        } else {
//            Screen.drawCenteredString(stack, Minecraft.getInstance().font, getTimeString(pump.timeLeft), 0, 5, 0xFFFFFFFF);
//        }

        stack.popPose();
    }

    private static String getTimeString(int ticks) {
        int seconds = ticks / 20;

        int i = (seconds % 3600) / 60;
        return (i > 0 ? i + "m " : "") + (seconds % 3600) % 60 + "s";
    }
}
