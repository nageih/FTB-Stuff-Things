package dev.ftb.mods.ftbobb.screens;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.TemperedJarBlockEntity;
import dev.ftb.mods.ftbobb.client.GuiUtil;
import dev.ftb.mods.ftbobb.network.StartJarCraftingPacket;
import dev.ftb.mods.ftbobb.temperature.TemperatureAndEfficiency;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TemperedJarScreen extends AbstractContainerScreen<TemperedJarMenu> {
    private static final ResourceLocation TEXTURE = FTBOBB.id("textures/gui/tempered_jar.png");
    private static final Rect2i FLUID_AREA = new Rect2i(55, 29, 48, 72);

    private Button startButton;

    public TemperedJarScreen(TemperedJarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        imageHeight = 218;
        inventoryLabelY = 125;
    }

    @Override
    protected void init() {
        super.init();

        startButton = Button.builder(Component.translatable("ftbobb.start_mix"), b -> StartJarCraftingPacket.sendToServer())
                .size(56, 20)
                .pos(leftPos + 112, topPos + 40)
                .build();
        addRenderableWidget(startButton);
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        startButton.active = menu.getJar().getProcessingTime() > 0;
        startButton.setMessage(Component.translatable(menu.getJar().getRemainingTime() >= 0 ? "ftbobb.stop_mix" : "ftbobb.start_mix"));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int minX = leftPos + FLUID_AREA.getX();
        int maxX = leftPos + FLUID_AREA.getX() + FLUID_AREA.getWidth();
        int minY = topPos + FLUID_AREA.getY();
        int maxY = topPos + FLUID_AREA.getY() + FLUID_AREA.getHeight();

        guiGraphics.fill(minX, minY, maxX, maxY, 0xFF8B8B8B);
        guiGraphics.blit(TEXTURE, minX - 4, minY - 13, 176, 0, 56, 90);

        renderTemperatureIndicator(guiGraphics, mouseX, mouseY, minX, maxY);
        renderFluids(guiGraphics, mouseX, mouseY);
        renderProgressBar(guiGraphics);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderTemperatureIndicator(GuiGraphics guiGraphics, int mouseX, int mouseY, int xPos, int yPos) {
        Rect2i tempIndicator = new Rect2i(xPos + (FLUID_AREA.getWidth() - 16) / 2, yPos + 6, 16, 16);
        TemperatureAndEfficiency temp = menu.getJar().getTemperature();
        guiGraphics.blit(temp.temperature().getTexture(),
                tempIndicator.getX(), tempIndicator.getY(),
                0, 0, 16, 16, 16, 16);
        if (tempIndicator.contains(mouseX, mouseY)) {
            List<Component> list = List.of(
                    Component.translatable("ftbobb.temperature", temp.temperature().getName()),
                    Component.translatable("ftbobb.efficiency", (int) (temp.efficiency() * 100))
            );
            guiGraphics.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
        }
    }

    private void renderProgressBar(GuiGraphics guiGraphics) {
        int remaining = menu.getJar().getRemainingTime();
        int total = menu.getJar().getProcessingTime();

        if (remaining >= 0 && total > 0) {
            int x1 = startButton.getX();
            int x2 = startButton.getX() + startButton.getWidth();
            int y1 = startButton.getY() + startButton.getHeight() + 2;
            int y2 = y1 + 8;
            guiGraphics.fill(x1 - 1, y1 - 1, x2 + 1, y2 + 1, 0xFF606060);
            guiGraphics.fill(x1 , y1, x2, y2, 0xFFA0A0A0);
            guiGraphics.blit(TEXTURE, x1, y1, 0, 240, (x2 - x1) * (total - remaining) / total, 8);
        }
    }

    private void renderFluids(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        IFluidHandler fluidHandler = menu.getJar().getFluidHandler();

        int yPos = FLUID_AREA.getY() + FLUID_AREA.getHeight();
        int total = fluidHandler.getTanks() * TemperedJarBlockEntity.TANK_CAPACITY;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(leftPos, topPos, 0);
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fs = fluidHandler.getFluidInTank(i);
            if (!fs.isEmpty()) {
                int ySize = FLUID_AREA.getHeight() * fs.getAmount() / total;
                yPos -= ySize;
                GuiUtil.drawFluid(guiGraphics, new Rect2i(FLUID_AREA.getX(), yPos, FLUID_AREA.getWidth(), ySize), fs, null);
            }
        }
        guiGraphics.pose().popPose();

        if (FLUID_AREA.contains(mouseX - leftPos, mouseY - topPos)) {
            List<Component> lines = new ArrayList<>();
            for (int i = fluidHandler.getTanks() - 1; i >= 0; i--) {
                FluidStack fs = fluidHandler.getFluidInTank(i);
                if (!fs.isEmpty()) {
                    lines.add(Component.translatable("ftblibrary.mb", fs.getAmount(), fs.getHoverName()));
                }
            }
            guiGraphics.renderTooltip(font, lines, Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (imageWidth - font.width(title)) / 2;
        guiGraphics.drawString(font, title, x, titleLabelY, 0xFF404040, false);
    }
}
