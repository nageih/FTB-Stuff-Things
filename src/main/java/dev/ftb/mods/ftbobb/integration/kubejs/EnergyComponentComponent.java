package dev.ftb.mods.ftbobb.integration.kubejs;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbobb.crafting.EnergyComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.rhino.type.TypeInfo;

public class EnergyComponentComponent implements RecipeComponent<EnergyComponent> {
    public static final EnergyComponentComponent ENERGY = new EnergyComponentComponent();

    @Override
    public Codec<EnergyComponent> codec() {
        return EnergyComponent.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(EnergyComponent.class);
    }

    @Override
    public String toString() {
        return "energy_component";
    }
}
