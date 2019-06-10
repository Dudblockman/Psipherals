package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.items.Items;
import com.teamwizardry.librarianlib.features.base.ModCreativeTab;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreativeTab extends ModCreativeTab {
    @NotNull
    @Override
    public ItemStack getIconStack() {
        return new ItemStack(Items.psimetalBow);
    }
}
