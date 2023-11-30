package com.oitsjustjose.itemgator.common.data.mod;

import com.oitsjustjose.itemgator.ItemGator;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeType implements RecipeType<ModRecipe> {
    @Override
    public String toString() {
        return ItemGator.MOD_ID + ":mod";
    }
}
