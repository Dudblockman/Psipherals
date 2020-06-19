package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.common.item.component.ItemCADComponent;

import java.util.List;

public class ItemShovelAssembly extends ItemCADComponent implements ICADAssembly {

    private final String model;
    public ItemShovelAssembly(Item.Properties props, String model) {
        super(props);
        this.model = model;
    }

    public static final String[] VARIANTS = {
            "shovel/shovel_assembly_iron",
            "shovel/shovel_assembly_gold",
            "shovel/shovel_assembly_psimetal",
            "shovel/shovel_assembly_ebony_psimetal",
            "shovel/shovel_assembly_ivory_psimetal",
            "shovel/shovel_assembly_creative"
    };
    public static final String[] MODELS = {
            "shovel/shovel"
            /*"shovel_iron",
            "shovel_gold",
            "shovel_psimetal",
            "shovel_ebony_psimetal",
            "shovel_ivory_psimetal",
            "shovel_creative"*/
    };

    @Override
    public ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        return ItemShovelCad.makeCAD(allComponents);
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
