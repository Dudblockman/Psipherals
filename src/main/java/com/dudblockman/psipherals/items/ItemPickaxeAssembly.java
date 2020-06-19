package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.common.item.component.ItemCADComponent;

import java.util.List;

public class ItemPickaxeAssembly extends ItemCADComponent implements ICADAssembly {
    private final String model;
    public ItemPickaxeAssembly(Item.Properties props, String model) {
        super(props);
        this.model = model;
    }

    public static final String[] VARIANTS = {
            "pickaxe/pickaxe_assembly_iron",
            "pickaxe/pickaxe_assembly_gold",
            "pickaxe/pickaxe_assembly_psimetal",
            "pickaxe/pickaxe_assembly_ebony_psimetal",
            "pickaxe/pickaxe_assembly_ivory_psimetal",
            "pickaxe/pickaxe_assembly_creative"
    };
    public static final String[] MODELS = {
            "pickaxe/pickaxe"
            /*"pickaxe_iron",
            "pickaxe_gold",
            "pickaxe_psimetal",
            "pickaxe_ebony_psimetal",
            "pickaxe_ivory_psimetal",
            "pickaxe_creative"*/
    };

    @Override
    public ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        return ItemAxeCad.makeCAD(allComponents);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
        return new ModelResourceLocation(Psipherals.MODID, model);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getCadTexture(ItemStack stack, ItemStack cad) {
        return new ResourceLocation(Psipherals.MODID, VARIANTS[Math.min(VARIANTS.length - 1, stack.getDamage())]);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.ASSEMBLY;
    }

}

