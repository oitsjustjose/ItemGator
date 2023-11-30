package com.oitsjustjose.itemgator.common.data.ingredient;

import com.oitsjustjose.itemgator.ItemGator;
import net.minecraft.world.item.crafting.RecipeType;

public class IngredientRecipeType implements RecipeType<IngredientRecipe> {
    @Override
    public String toString() {
        return ItemGator.MOD_ID + ":ingredient";
    }
}
