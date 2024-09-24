package dev.ftb.mods.ftbobb.integration.jei;

import dev.ftb.mods.ftbobb.temperature.Temperature;
import mezz.jei.api.ingredients.IIngredientType;

public class OBBIngredientTypes {
	public static final IIngredientType<Temperature> TEMPERATURE = () -> Temperature.class;
}
