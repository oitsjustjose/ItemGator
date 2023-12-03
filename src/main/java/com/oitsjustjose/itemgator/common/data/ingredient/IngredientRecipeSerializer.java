package com.oitsjustjose.itemgator.common.data.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class IngredientRecipeSerializer implements RecipeSerializer<IngredientRecipe> {
    @Override
    public @NotNull IngredientRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject obj) {
        var input = Ingredient.fromJson(obj.get("input"), false);
        var substitute = ShapedRecipe.itemStackFromJson(obj.getAsJsonObject("substitute"));
        substitute.setCount(1);
        List<String> tags = Lists.newArrayList();

        if (obj.has("tags")) {
            obj.getAsJsonArray("tags").forEach(x -> tags.add(x.getAsString()));
        } else {
            tags.add(obj.get("tag").getAsString());
        }

        var exceptions = obj.has("exceptions") ? Ingredient.fromJson(obj.get("exceptions"), false) : null;
        return new IngredientRecipe(id, input, substitute, tags, exceptions);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull IngredientRecipe recipe) {
        CraftingHelper.write(buf, recipe.getIngredient());
        buf.writeItemStack(recipe.getPlainSubstitute(), false);
        buf.writeUtf(String.join(",", recipe.getTags()));

        var exceptions = recipe.getExceptions();
        buf.writeBoolean(exceptions != null);
        if (exceptions != null) {
            CraftingHelper.write(buf, exceptions);
        }
    }

    @Override
    public @Nullable IngredientRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        var ingredient = Ingredient.fromNetwork(buf);
        var substitute = buf.readItem();
        var tags = Arrays.stream(buf.readUtf().split(",")).toList();

        var hasExceptions = buf.readBoolean();
        var exceptions = hasExceptions ? Ingredient.fromNetwork(buf) : null;
        return new IngredientRecipe(id, ingredient, substitute, tags, exceptions);
    }
}
