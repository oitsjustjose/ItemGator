package com.oitsjustjose.itemgator.common.data;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class BaseRecipe {
    public abstract boolean matchesOriginal(ItemStack stackIn);

    public abstract boolean matchesSubstitute(ItemStack stackIn);

    public abstract boolean appliesTo(Player player);

    public abstract ItemStack getSubstitute(ItemStack original);

    public abstract ItemStack getOriginal(ItemStack substitute);

}
