package com.dudblockman.psipherals.items.armor;

import com.dudblockman.psipherals.util.PsiAttributes;
import com.dudblockman.psipherals.util.libs.AdvPsimetalArmorMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import vazkii.psi.common.item.armor.ItemPsimetalArmor;

import javax.annotation.Nullable;
import java.util.UUID;

public class ItemPsimetalHardsuit extends ItemPsimetalArmor {
    public ItemPsimetalHardsuit(EquipmentSlotType type, Properties props) {
        super(type, AdvPsimetalArmorMaterial.INSTANCE, props);
    }
    private static CompoundNBT composeTag(Attribute attributeName, AttributeModifier modifier, @Nullable EquipmentSlotType equipmentSlot) {
        CompoundNBT compoundnbt = modifier.write();
        compoundnbt.putString("AttributeName", Registry.ATTRIBUTE.getKey(attributeName).toString());
        if (equipmentSlot != null) {
            compoundnbt.putString("Slot", equipmentSlot.getName());
        }
        return compoundnbt;
    }
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        int pieces = 0;
        boolean isEquipped = false;
        Iterable<ItemStack> stacks = entityIn.getArmorInventoryList();
        for (ItemStack s : stacks) {
            if (s.equals(stack)) {
                isEquipped = true;
            } else if (s.getItem() instanceof ItemPsimetalHardsuit) {
                pieces++;
            }
        }
        if (!isEquipped) {
            pieces = 0;
        }

        stack.getOrCreateTag();
        CompoundNBT tag = stack.getTag();
        if (!tag.contains("AttributeModifiers", 9)) {
            tag.put("AttributeModifiers", new ListNBT());
        }

        ListNBT listnbt = tag.getList("AttributeModifiers", 10);
        listnbt.clear();

        UUID uuid = ArmorItem.ARMOR_MODIFIERS[slot.getIndex()];
        int minResistance = AdvPsimetalArmorMaterial.DAMAGE_REDUCTION_ARRAY_MIN[slot.getIndex()];
        int maxResistance = AdvPsimetalArmorMaterial.DAMAGE_REDUCTION_ARRAY_MAX[slot.getIndex()];
        int resistance = (int) (minResistance + (pieces / 3.0) * (maxResistance-minResistance));
        int toughness = (int) ((pieces / 3.0) * AdvPsimetalArmorMaterial.MAX_TOUGHNESS);
        float knockback = ((1+pieces) / 4.0F) * AdvPsimetalArmorMaterial.MAX_KNOCKBACK;
        int capacity = (int) ((pieces / 3.0) * AdvPsimetalArmorMaterial.CAPACITY_BOOST);
        int regen = (int) ((pieces / 3.0) * AdvPsimetalArmorMaterial.REGEN_BOOST);

        listnbt.add(composeTag(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", resistance, AttributeModifier.Operation.ADDITION),super.type));
        listnbt.add(composeTag(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", toughness, AttributeModifier.Operation.ADDITION),super.type));
        listnbt.add(composeTag(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", knockback, AttributeModifier.Operation.ADDITION),super.type));
        listnbt.add(composeTag(PsiAttributes.PSI_CAPACITY, new AttributeModifier(uuid, "Psi capacity boost", capacity, AttributeModifier.Operation.ADDITION),super.type));
        listnbt.add(composeTag(PsiAttributes.PSI_REGEN, new AttributeModifier(uuid, "Psi regen boost", regen, AttributeModifier.Operation.ADDITION),super.type));

    }
}
