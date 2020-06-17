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
    public void registerStats() {
        // Iron
        addStat(EnumCADStat.EFFICIENCY, 0, 70);
        addStat(EnumCADStat.POTENCY, 0, 100);

        // Gold
        addStat(EnumCADStat.EFFICIENCY, 1, 65);
        addStat(EnumCADStat.POTENCY, 1, 150);

        // Psimetal
        addStat(EnumCADStat.EFFICIENCY, 2, 80);
        addStat(EnumCADStat.POTENCY, 2, 250);

        // Ebony Psimetal
        addStat(EnumCADStat.EFFICIENCY, 3, 90);
        addStat(EnumCADStat.POTENCY, 3, 350);

        // Ivory Psimetal
        addStat(EnumCADStat.EFFICIENCY, 4, 95);
        addStat(EnumCADStat.POTENCY, 4, 320);

        // Creative
        addStat(EnumCADStat.EFFICIENCY, 5, -1);
        addStat(EnumCADStat.POTENCY, 5, -1);
    }

    @Override
    public ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        return ItemAxeCad.makeCAD(allComponents);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
        return new ModelResourceLocation(Psipherals.MODID, MODELS[Math.min(MODELS.length - 1, stack.getDamage())]);
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
