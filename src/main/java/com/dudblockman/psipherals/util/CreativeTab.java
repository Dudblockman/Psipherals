package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.items.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTab extends ItemGroup {

    public static final CreativeTab INSTANCE = new CreativeTab();

    public CreativeTab() {
        super(Psipherals.MODID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.psimetalBow);
    }
}
