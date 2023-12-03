package com.oitsjustjose.itemgator;


import com.oitsjustjose.itemgator.common.data.RecipeTypeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Mod(ItemGator.MOD_ID)
public class ItemGator {
    public static final String MOD_ID = "itemgator";
    private static ItemGator instance;
    public final Logger LOGGER = LogManager.getLogger();
    public final RecipeTypeRegistry CustomRecipeRegistry = new RecipeTypeRegistry();


    public ItemGator() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);

        CustomRecipeRegistry.SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void onReload(AddReloadListenerEvent event) {
        CustomRecipeRegistry.reset();
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.player.isCreative()) return;
        if (event.player.level().isClientSide()) return;

        var inventory = event.player.getInventory();
        var allRecipes = this.CustomRecipeRegistry.getAllRecipes();

        Arrays.asList(inventory.items, inventory.armor, inventory.offhand).forEach(compartment -> {
            for (int i = 0; i < compartment.size(); i++) {
                var stack = compartment.get(i);
                if (stack.isEmpty()) continue;

                // Check to see if we should be replacing this item with a substitute
                var shouldReplace = allRecipes.parallelStream().filter(x -> x.matchesOriginal(stack) && x.appliesTo(event.player)).findFirst();
                if (shouldReplace.isPresent()) {
                    var sub = shouldReplace.get().getSubstitute(stack);
                    inventory.setItem(i, ItemStack.EMPTY);
                    inventory.add(sub);
                    continue;
                }

                // See if this is a substitute and if we should be replacing this item with its original because the player has the needed tag
                var shouldRevert = allRecipes.parallelStream().filter(x -> x.matchesSubstitute(stack) && !x.appliesTo(event.player)).findFirst();
                if (shouldRevert.isPresent()) {
                    var orig = shouldRevert.get().getOriginal(stack);
                    inventory.setItem(i, ItemStack.EMPTY);
                    inventory.add(orig);
                }
            }
        });
    }

    public static ItemGator getInstance() {
        return instance;
    }
}
