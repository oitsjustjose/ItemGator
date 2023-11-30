package com.oitsjustjose.itemgator.common.data.mod;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModRecipeSerializer implements RecipeSerializer<ModRecipe> {
    @Override
    public @NotNull ModRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject obj) {
        // TODO: better validation

        var mod = obj.get("mod").getAsString();
        var substitute = ShapedRecipe.itemStackFromJson(obj.getAsJsonObject("substitute"));
        substitute.setCount(1);
        List<String> tags = Lists.newArrayList();

        if (obj.has("tags")) {
            obj.getAsJsonArray("tags").forEach(x -> tags.add(x.getAsString()));
        } else {
            tags.add(obj.get("tag").getAsString());
        }

        return new ModRecipe(id, mod, substitute, tags);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull ModRecipe recipe) {
        buf.writeUtf(recipe.getMod());
        buf.writeItemStack(recipe.getPlainSubstitute(), false);
        buf.writeUtf(recipe.getTags().stream().collect(Collectors.joining(",")));
    }

    @Override
    public @Nullable ModRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        var mod = buf.readUtf();
        var substitute = buf.readItem();
        var tags = Arrays.stream(buf.readUtf().split(",")).toList();
        return new ModRecipe(id, mod, substitute, tags);
    }
}
