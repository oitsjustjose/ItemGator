package com.oitsjustjose.itemgator.common.data;

import com.oitsjustjose.itemgator.ItemGator;
import com.oitsjustjose.itemgator.common.data.ingredient.IngredientRecipe;
import com.oitsjustjose.itemgator.common.data.ingredient.IngredientRecipeSerializer;
import com.oitsjustjose.itemgator.common.data.ingredient.IngredientRecipeType;
import com.oitsjustjose.itemgator.common.data.mod.ModRecipe;
import com.oitsjustjose.itemgator.common.data.mod.ModRecipeSerializer;
import com.oitsjustjose.itemgator.common.data.mod.ModRecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class RecipeTypeRegistry {
    public final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ItemGator.MOD_ID);
    public final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ItemGator.MOD_ID);

    public final RecipeSerializer<ModRecipe> MOD;
    public final RecipeType<ModRecipe> MOD_TYPE;

    public final RecipeSerializer<IngredientRecipe> INGREDIENT;
    public final RecipeType<IngredientRecipe> INGREDIENT_TYPE;


    private final List<BaseRecipe> allRecipes;

    public RecipeTypeRegistry() {
        this.allRecipes = Lists.newArrayList();

        this.MOD = new ModRecipeSerializer();
        this.MOD_TYPE = new ModRecipeType();
        SERIALIZERS.register("mod", () -> this.MOD);
        TYPES.register("mod", () -> this.MOD_TYPE);

        this.INGREDIENT = new IngredientRecipeSerializer();
        this.INGREDIENT_TYPE = new IngredientRecipeType();
        SERIALIZERS.register("ingredient", () -> this.INGREDIENT);
        TYPES.register("ingredient", () -> this.INGREDIENT_TYPE);
    }

    public List<BaseRecipe> getAllRecipes() {
        return this.allRecipes;
    }

    public void reset() {
        this.allRecipes.clear();
    }

    public void registerRecipe(BaseRecipe recipe) {
        this.allRecipes.add(recipe);
    }
}
