package com.dudblockman.psipherals.jei;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.block.base.Blocks;
import com.dudblockman.psipherals.crafting.CraftingRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEICompatibility implements IModPlugin {
    private static final ResourceLocation UID = Psipherals.location("main");
    public static IJeiHelpers helpers;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        helpers = registry.getJeiHelpers();
        registry.addRecipeCategories(new PsilonInfusionCategory(helpers.getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(Minecraft.getInstance().world.getRecipeManager().getRecipes(CraftingRecipes.INFUSION_TYPE).values(), PsilonInfusionCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.psilon.asItem()), PsilonInfusionCategory.UID);
    }
}
