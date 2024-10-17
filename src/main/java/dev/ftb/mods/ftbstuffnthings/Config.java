package dev.ftb.mods.ftbstuffnthings;

import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;

import static dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil.CONFIG_DIR;
import static dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil.loadDefaulted;

public class Config {
    private static final SNBTConfig CONFIG = SNBTConfig.create("ftb-obb");

    private static final SNBTConfig GENERAL_CONFIG = CONFIG.addGroup("general");
    public static final BooleanValue INCLUDE_DEV_RECIPES = GENERAL_CONFIG.addBoolean("include_dev_recipes", false)
            .comment("If true, dev/testing recipes will be available outside a development environment", "Leave this false unless actually testing the mod.");

    private static final SNBTConfig SLUICE_CONFIG = CONFIG.addGroup("sluice");
    public static final SNBTConfig OAK_SLUICE_CONFIG = createSluiceConfig(SLUICE_CONFIG, "oakSluice", 1, 1, 12000, false, false, false, 0);
    public static final SNBTConfig IRON_SLUICE_CONFIG = createSluiceConfig(SLUICE_CONFIG, "ironSluice", .8, .6, 12000, true, false, false, 0);
    public static final SNBTConfig DIAMOND_SLUICE_CONFIG = createSluiceConfig(SLUICE_CONFIG, "diamondSluice", .6, .75, 12000, true, true, false, 0);
    public static final SNBTConfig NETHERITE_SLUICE_CONFIG = createSluiceConfig(SLUICE_CONFIG, "netheriteSluice", .4, .5, 12000, true, true, true, 40);

    private static final SNBTConfig AUTOHAMMER_CONFIG = CONFIG.addGroup("autohammer");
    public static final IntValue IRON_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("stone_hammer_speed", 50, 1, 100000)
            .comment("Speed of the iron auto-hammer as ticks taken to process a block");
    public static final IntValue GOLD_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("gold_hammer_speed", 40, 1, 100000)
            .comment("Speed of the gold auto-hammer as ticks taken to process a block");
    public static final IntValue DIAMOND_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("diamond_hammer_speed", 30, 1, 100000)
            .comment("Speed of the diamond auto-hammer as ticks taken to process a block");
    public static final IntValue NETHERITE_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("netherite_hammer_speed", 15, 1, 100000)
            .comment("Speed of the netherite auto-hammer as ticks taken to process a block");

    private static final SNBTConfig COBBLEGEN_CONFIG = CONFIG.addGroup("cobblegen");

    public static final IntValue DELAY_PER_OPERATION = COBBLEGEN_CONFIG.addInt("operations_per_tick", 20, 1, Integer.MAX_VALUE)
            .comment("The delay between each cobble generation in ticks");

    public static final IntValue STONE_COBBLEGEN_AMOUNT = COBBLEGEN_CONFIG.addInt("stone_cobblegen_amount", 1, 1, 1000)
            .comment("Amount of cobble the stone cobblegen produces per tick");
    public static final IntValue IRON_COBBLEGEN_AMOUNT = COBBLEGEN_CONFIG.addInt("iron_cobblegen_amount", 8, 1, 1000)
            .comment("Amount of cobble the iron cobblegen produces per tick");
    public static final IntValue GOLD_COBBLEGEN_AMOUNT = COBBLEGEN_CONFIG.addInt("gold_cobblegen_amount", 16, 1, 1000)
            .comment("Amount of cobble the gold cobblegen produces per tick");
    public static final IntValue DIAMOND_COBBLEGEN_AMOUNT = COBBLEGEN_CONFIG.addInt("diamond_cobblegen_amount", 32, 1, 1000)
            .comment("Amount of cobble the diamond cobblegen produces per tick");
    public static final IntValue NETHERITE_COBBLEGEN_AMOUNT = COBBLEGEN_CONFIG.addInt("netherite_cobblegen_amount", 64, 1, 1000)
            .comment("Amount of cobble the netherite cobblegen produces per tick");

    public static void init() {
        loadDefaulted(CONFIG, CONFIG_DIR, FTBStuffNThings.MODID, FTBStuffNThings.MODID + ".snbt");
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
