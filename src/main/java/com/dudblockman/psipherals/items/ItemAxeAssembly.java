package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.common.item.component.ItemCADComponent;

import java.util.List;

public class ItemAxeAssembly extends ItemCADComponent implements ICADAssembly {
    private final String model;
    public ItemAxeAssembly(Item.Properties props, String model) {
        super(props);
        this.model = model;
    }

    public static final String[] VARIANTS = {
            "axe/axe_assembly_iron",
            "axe/axe_assembly_gold",
            "axe/axe_assembly_psimetal",
            "axe/axe_assembly_ebony_psimetal",
            "axe/axe_assembly_ivory_psimetal",
            "axe/axe_assembly_creative"
    };
    public static final String[] MODELS = {
            "axe/axe"
            /*"axe_iron",
            "axe_gold",
            "axe_psimetal",
            "axe_ebony_psimetal",
            "axe_ivory_psimetal",
            "axe_creative"*/
    };

    @Override
    public ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        return ItemAxeCad.makeCAD(allComponents);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
        return Psipherals.location("axe");
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
