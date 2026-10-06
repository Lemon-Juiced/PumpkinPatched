package site.scalarstudios.pumpkinpatched.item;

import net.minecraft.world.food.FoodProperties;

public class PPFoods {
    public static final FoodProperties BAKED_PUMPKIN_SEEDS = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).alwaysEdible().build();
    public static final FoodProperties PUMPKIN_BREAD = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build();
    public static final FoodProperties SPOOKY_STEW = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).alwaysEdible().build();
}
