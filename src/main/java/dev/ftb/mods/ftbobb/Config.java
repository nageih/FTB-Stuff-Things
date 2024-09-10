package dev.ftb.mods.ftbobb;

import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;

public class Config {
    private static final SNBTConfig sluiceConfig = SNBTConfig.create("ftb-obb");

    public static final SNBTConfig oakSluiceConfig = createSluiceConfig(sluiceConfig, "oakSluice", 1, 1, 12000, false, false, false, 0);
    public static final SNBTConfig ironSluiceConfig = createSluiceConfig(sluiceConfig, "ironSluice", .8, .6, 12000, true, false, false, 0);
    public static final SNBTConfig diamondSluiceConfig = createSluiceConfig(sluiceConfig, "diamondSluice", .6, .75, 12000, true, true, false, 0);
    public static final SNBTConfig netheriteSluiceConfig = createSluiceConfig(sluiceConfig, "netheriteSluice", .4, .5, 12000, true, true, true, 40);

    public static void init() {
        // TODO: Implement
    }

    private static SNBTConfig createSluiceConfig(SNBTConfig parent, String name, double timeMod, double fluidMod, int tankCap, boolean allowsIO, boolean allowsTank, boolean upgradeable, int energyCost) {
        SNBTConfig config = parent.addGroup(name);

        config.addDouble("processing time multiplier", timeMod).comment("Defines how long it takes to process a resource in this Sluice (This is multiplied by the recipe base tick time)");
        config.addDouble("fluid multiplier", fluidMod).comment("Sets how much fluid is used per processed recipe (This is multiplied by the recipe's fluid consumption rate)");
        config.addInt("tank capacity", tankCap).comment("Sets how much fluid this sluice's tank can carry (in mB)");
        config.addBoolean("allowsIO", allowsIO).comment("Allows this sluice to be used for item IO");
        config.addBoolean("allowsTank", allowsTank).comment("Allows this sluice to be used for fluid IO");
        config.addBoolean("upgradeable", upgradeable).comment("Allows this sluice to be upgraded");
        config.addInt("fe cost per use", energyCost).comment("FE cost per use");

        return config;
    }
}
