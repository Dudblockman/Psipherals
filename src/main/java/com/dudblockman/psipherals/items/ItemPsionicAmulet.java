package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.capability.AmuletCapabilityProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemPsionicAmulet extends Item {

    public ItemPsionicAmulet(Item.Properties props) {
        super(props.maxStackSize(1).defaultMaxDamage(0));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new AmuletCapabilityProvider(stack,1);
    }

}

