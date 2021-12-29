package com.dudblockman.psipherals.util.libs;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.material.PsimetalArmorMaterial;

public class AdvPsimetalArmorMaterial implements IArmorMaterial {
    public static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
    public static final int[] DAMAGE_REDUCTION_ARRAY_MIN = new int[] { 2, 5, 6, 2 };
    public static final int[] DAMAGE_REDUCTION_ARRAY_MAX = new int[] { 3, 6, 8, 3 };
    public static final int MAX_TOUGHNESS = 4;
    public static final float MAX_KNOCKBACK = 0.2F;
    public static final int CAPACITY_BOOST = 1250;
    public static final int REGEN_BOOST = 6;
    public static final AdvPsimetalArmorMaterial INSTANCE = new AdvPsimetalArmorMaterial();
    private static final LazyValue<Ingredient> REPAIR_MATERIAL = new LazyValue<>(
            () -> Ingredient.fromItems(Registry.ITEM.getOrDefault(new ResourceLocation(PsiAPI.MOD_ID, "psimetal"))));

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * 37;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return DAMAGE_REDUCTION_ARRAY_MIN[slotIn.getIndex()] ;
    }

    @Override
    public int getEnchantability() {
        return 14;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return null;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return REPAIR_MATERIAL.getValue();
    }

    @Override
    public String getName() {
        return "psimetalHardsuit";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.15F;
    }
}
