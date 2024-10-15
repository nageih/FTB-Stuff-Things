package dev.ftb.mods.ftbstuffnthings.integration.jei;

import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import mezz.jei.api.ingredients.IIngredientType;

public class FTBStuffIngredientTypes {
	public static final IIngredientType<Temperature> TEMPERATURE = () -> Temperature.class;
}
