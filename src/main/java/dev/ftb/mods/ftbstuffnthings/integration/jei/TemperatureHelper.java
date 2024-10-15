package dev.ftb.mods.ftbstuffnthings.integration.jei;

import com.google.common.base.MoreObjects;
import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public enum TemperatureHelper implements IIngredientHelper<Temperature> {
    INSTANCE;

    @Override
    public IIngredientType<Temperature> getIngredientType() {
        return OBBIngredientTypes.TEMPERATURE;
    }

    @Override
    public String getDisplayName(Temperature ingredient) {
        return ingredient.getName().getString();
    }

    @Override
    public String getUniqueId(Temperature ingredient, UidContext context) {
        return ingredient.getSerializedName();
    }

    @Override
    public Object getUid(Temperature ingredient, UidContext context) {
        return ingredient.getSerializedName();
    }

    @Override
    public ResourceLocation getResourceLocation(Temperature ingredient) {
        return FTBStuffNThings.id(ingredient.getSerializedName());
    }

    @Override
    public Temperature copyIngredient(Temperature ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable Temperature ingredient) {
        if (ingredient == null) {
            return "null";
        }

        return MoreObjects.toStringHelper(Temperature.class).add("ID", ingredient.getSerializedName()).toString();
    }
}
