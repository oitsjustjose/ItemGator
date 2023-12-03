package com.oitsjustjose.itemgator.common.data.mod;

import com.oitsjustjose.itemgator.ItemGator;
import com.oitsjustjose.itemgator.common.data.BaseRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModRecipe extends BaseRecipe implements Recipe<RecipeWrapper> {
    public final ResourceLocation id;
    private final String mod;
    private final ItemStack substitute;
    private final List<String> tags;
    private final @Nullable Ingredient exceptions;

    public ModRecipe(ResourceLocation id, String mod, ItemStack substitute, List<String> tags, @Nullable Ingredient exceptions) {
        this.id = id;
        this.mod = mod;
        this.substitute = substitute;
        this.tags = tags;
        this.exceptions = exceptions;
        ItemGator.getInstance().CustomRecipeRegistry.registerRecipe(this);
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
        var stack = wrapper.getItem(0);
        return this.matchesOriginal(stack);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper unused, @NotNull RegistryAccess unused2) {
        return this.substitute;
    }

    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess unused) {
        return this.substitute;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ItemGator.getInstance().CustomRecipeRegistry.MOD;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ItemGator.getInstance().CustomRecipeRegistry.MOD_TYPE;
    }

    @Override
    public boolean matchesOriginal(ItemStack stackIn) {
        if (exceptions != null && exceptions.test(stackIn)) return false;
        var rl = ForgeRegistries.ITEMS.getKey(stackIn.getItem());
        return rl != null && rl.getNamespace().equals(this.mod);
    }

    @Override
    public boolean matchesSubstitute(ItemStack stackIn) {
        return stackIn.getItem() == this.substitute.getItem();
    }

    @Override
    public ItemStack getSubstitute(ItemStack original) {
        var stack = substitute.copy();
        stack.setCount(original.getCount());
        original.setCount(1);

        var tag = stack.getOrCreateTag();
        tag.put("original", original.serializeNBT());
        stack.setTag(tag);
        return stack;
    }

    @Override
    public ItemStack getOriginal(ItemStack substitute) {
        if (!substitute.hasTag()) {
            ItemGator.getInstance().LOGGER.error("Provided substitute {} does not have a tag - that item is likely lost forever!", substitute.getDisplayName());
            return substitute;
        }

        var tag = substitute.getTag();
        if (tag == null) {
            ItemGator.getInstance().LOGGER.error("Provided substitute {} does not have a tag - that item is likely lost forever!", substitute.getDisplayName());
            return substitute;
        }

        var original = ItemStack.of(tag.getCompound("original"));
        original.setCount(substitute.getCount());
        return original;
    }

    @Override
    public boolean appliesTo(Player player) {
        for (var tag : this.tags) {
            if (!player.getTags().contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public String getMod() {
        return mod;
    }

    public ItemStack getPlainSubstitute() { return this.substitute.copy(); }

    public @Nullable Ingredient getExceptions() { return this.exceptions; }
}
