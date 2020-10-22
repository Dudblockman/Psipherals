package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.common.item.component.ItemCADComponent;

import java.util.List;

public class ItemPickaxeAssembly extends ItemCADComponent implements ICADAssembly {
    private final String model;
    public ItemPickaxeAssembly(Item.Properties props, String model) {
        super(props);
        this.model = model;
    }

    @Override
    public ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        ListNBT enchants = stack.getEnchantmentTagList();
        ItemStack CAD = ItemPickaxeCad.makeCAD(allComponents);
        CAD.getOrCreateTag();
        CAD.getTag().put("Enchantments", enchants);
        return CAD;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
        return Psipherals.location("item/" + model);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getCadTexture(ItemStack stack, ItemStack cad) {
        return Psipherals.location(model);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.ASSEMBLY;
    }

}

