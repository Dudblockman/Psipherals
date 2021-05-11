package com.dudblockman.psipherals.util.libs;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vazkii.psi.api.PsiAPI;

public class AdvPsimetalToolMaterial implements IItemTier {
    private static final LazyValue<Ingredient> REPAIR_MATERIAL = new LazyValue<>(
            () -> Ingredient.fromItems(Registry.ITEM.getOrDefault(new ResourceLocation(PsiAPI.MOD_ID, "psimetal"))));

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
    public Ingredient getRepairMaterial() { return REPAIR_MATERIAL.getValue(); }
}
