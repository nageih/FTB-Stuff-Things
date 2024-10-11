package dev.ftb.mods.ftbobb.integration.kubejs;

import dev.ftb.mods.ftbobb.FTBOBB;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;

public class KubeJSIntegration implements KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(FTBOBB.id("jar"), JarRecipeSchema.SCHEMA);
        registry.register(FTBOBB.id("hammer"), HammerRecipeSchema.SCHEMA);
        registry.register(FTBOBB.id("crook"), CrookRecipeSchema.SCHEMA);
        registry.register(FTBOBB.id("fusing_machine"), FusingMachineRecipeSchema.SCHEMA);
        registry.register(FTBOBB.id("super_cooler"), SuperCoolerRecipeSchema.SCHEMA);

        FTBOBB.LOGGER.info("Registered KubeJS recipe schemas");
    }
}
