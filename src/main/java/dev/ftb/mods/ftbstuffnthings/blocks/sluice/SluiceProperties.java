package dev.ftb.mods.ftbstuffnthings.blocks.sluice;

import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.DoubleValue;
import dev.ftb.mods.ftblibrary.snbt.config.IntValue;

public record SluiceProperties(DoubleValue timeMod, DoubleValue fluidMod, IntValue tankCap,
                               BooleanValue itemIO, BooleanValue fluidIO, BooleanValue upgradeable, IntValue energyCost) {
}
