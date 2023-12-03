package com.oitsjustjose.itemgator.common.data.mod;

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

public class ModRecipeSerializer implements RecipeSerializer<ModRecipe> {
    @Override
    public @NotNull ModRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject obj) {
        var mod = obj.get("mod").getAsString();
        var substitute = ShapedRecipe.itemStackFromJson(obj.getAsJsonObject("substitute"));
        substitute.setCount(1);
        List<String> tags = Lists.newArrayList();

        if (obj.has("tags")) {
            obj.getAsJsonArray("tags").forEach(x -> tags.add(x.getAsString()));
        } else {
            tags.add(obj.get("tag").getAsString());
        }

        var exceptions = obj.has("exceptions") ? Ingredient.fromJson(obj.get("exceptions"), false) : null;
        return new ModRecipe(id, mod, substitute, tags, exceptions);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull ModRecipe recipe) {
        buf.writeUtf(recipe.getMod());
        buf.writeItemStack(recipe.getPlainSubstitute(), false);
        buf.writeUtf(String.join(",", recipe.getTags()));

        var exceptions = recipe.getExceptions();
        buf.writeBoolean(exceptions != null);
        if (exceptions != null) {
            CraftingHelper.write(buf, exceptions);
        }
    }

    @Override
    public @Nullable ModRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        var mod = buf.readUtf();
        var substitute = buf.readItem();
        var tags = Arrays.stream(buf.readUtf().split(",")).toList();

        var hasExceptions = buf.readBoolean();
        var exceptions = hasExceptions ? Ingredient.fromNetwork(buf) : null;
        return new ModRecipe(id, mod, substitute, tags, exceptions);
    }
}
