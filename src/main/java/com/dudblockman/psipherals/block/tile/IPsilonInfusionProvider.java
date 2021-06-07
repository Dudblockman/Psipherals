package com.dudblockman.psipherals.block.tile;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.internal.Vector3;

public interface IPsilonInfusionProvider {
    int activate(Vector3 inputFrequency);

    ItemStack getHeldItem();

    void consumeItem();

    void replaceItem(ItemStack newStack);
    //I should make this all extensible but who really is going to make an addon to an addon to a mod to minecraft?
}
