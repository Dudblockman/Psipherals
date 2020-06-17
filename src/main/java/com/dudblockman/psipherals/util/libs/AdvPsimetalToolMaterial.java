package com.dudblockman.psipherals.util.libs;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class AdvPsimetalToolMaterial implements IItemTier {
    @Override
    public int getMaxUses() {
    return 900;
}

    @Override
    public float getEfficiency() {
        return 8.8F;
    }

    @Override
    public float getAttackDamage() {
        return 3F;
    }

    @Override
    public int getHarvestLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 14;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromTag(ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "ingots/psimetal")));
    }
}
