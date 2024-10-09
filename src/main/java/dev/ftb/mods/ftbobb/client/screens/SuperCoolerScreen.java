package dev.ftb.mods.ftbobb.client.screens;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.ftb.mods.ftbobb.blocks.supercooler.SuperCoolerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class SuperCoolerScreen extends BaseFluidAndEnergyScreen<SuperCoolerMenu> {
    private static final ResourceLocation TEXTURE = FTBOBB.id("textures/gui/super_cooler_background.png");

    public SuperCoolerScreen(SuperCoolerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 3, 79, TEXTURE);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 155 - font.width(title);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int i, int j) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
    }

    @Override
    public int getEnergyAmount() {
        return menu.blockEntity.getEnergy();
    }

    @Override
    public int getEnergyCapacity() {
        return this.menu.blockEntity.getMaxEnergy();
    }

    @Override
    public int getFluidCapacity() {
        return this.menu.blockEntity.getMaxFluid();
    }

    @Override
    public FluidStack getFluidStack() {
        return this.menu.blockEntity.getFluid();
    }

    @Override
    public int getProgress() {
        return menu.blockEntity.getProgress();
    }

    @Override
    public int getProgressRequired() {
        return menu.blockEntity.getMaxProgress();
    }
}
