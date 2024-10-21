package dev.ftb.mods.ftbstuffnthings;

import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceProperties;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceType;
import net.minecraft.Util;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.EnumMap;
import java.util.Map;

import static dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil.CONFIG_DIR;
import static dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil.loadDefaulted;

public class Config {
    private static final SNBTConfig CONFIG = SNBTConfig.create(FTBStuffNThings.MODID);

    private static final SNBTConfig GENERAL_CONFIG = CONFIG.addGroup("general");
    public static final BooleanValue INCLUDE_DEV_RECIPES = GENERAL_CONFIG.addBoolean("include_dev_recipes", false)
            .comment("If true, dev/testing recipes will be available outside a development environment", "Leave this false unless actually testing the mod.");

    private static final SNBTConfig SLUICE_CONFIG = CONFIG.addGroup("sluice");
    private static final Map<SluiceType,SNBTConfig> SLUICE_TYPES = Util.make(new EnumMap<>(SluiceType.class), map -> {
        for (SluiceType type : SluiceType.values()) {
            map.put(type, SLUICE_CONFIG.addGroup(type.getSerializedName()));
        }
    });

    private static final SNBTConfig AUTOHAMMER_CONFIG = CONFIG.addGroup("autohammer");
    public static final IntValue IRON_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("stone_hammer_speed", 50, 1, 100000)
            .comment("Speed of the iron auto-hammer as ticks taken to process a block");
    public static final IntValue GOLD_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("gold_hammer_speed", 40, 1, 100000)
            .comment("Speed of the gold auto-hammer as ticks taken to process a block");
    public static final IntValue DIAMOND_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("diamond_hammer_speed", 30, 1, 100000)
            .comment("Speed of the diamond auto-hammer as ticks taken to process a block");
    public static final IntValue NETHERITE_HAMMER_SPEED = AUTOHAMMER_CONFIG.addInt("netherite_hammer_speed", 15, 1, 100000)
            .comment("Speed of the netherite auto-hammer as ticks taken to process a block");

    public static void init() {
        loadDefaulted(CONFIG, CONFIG_DIR, FTBStuffNThings.MODID, FTBStuffNThings.MODID + ".snbt");
    }

    public static Lazy<SluiceProperties> makeSluiceProperties(SluiceType type) {
        SNBTConfig config = SLUICE_TYPES.get(type);

        return Lazy.of(() -> new SluiceProperties(
                config.addDouble("processing time multiplier", type.defTimeMod)
                        .comment("How long it takes to process a resource in this Sluice (multiplier for recipe base tick time)"),
                config.addDouble("fluid multiplier", type.defFluidMod)
                        .comment("How much fluid is used per recipe (multiplier for recipe's fluid consumption rate)"),
                config.addInt("tank capacity", type.defCapacity)
                        .comment("How much fluid this sluice's tank can carry (in mB)"),
                config.addBoolean("allowsIO", type.defItemIO)
                        .comment("Can items be piped in? False = items only clicked in manually"),
                config.addBoolean("allowsTank", type.defFluidIO)
                        .comment("Can fluid be piped in? False = need to use adjacent Pump"),
                config.addBoolean("upgradeable", type.defUpgradeable)
                        .comment("Can sluice be upgraded?"),
                config.addInt("fe cost per use", type.defEnergyUsage)
                        .comment("FE cost per use")
        ));
    }
}
