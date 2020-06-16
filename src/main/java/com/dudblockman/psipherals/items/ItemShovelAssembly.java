package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.teamwizardry.librarianlib.core.client.ModelHandler;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADAssembly;

import java.util.List;

public class ItemShovelAssembly extends ItemAdvComponent implements ICADAssembly, IExtraVariantHolder {
    public ItemShovelAssembly(String name){
        super(name, VARIANTS);
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
        return ItemShovelCad.makeCAD(allComponents);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad) {
        return ModelHandler.INSTANCE.getResource(Psipherals.MODID, MODELS[Math.min(MODELS.length - 1, stack.getItemDamage())]);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.ASSEMBLY;
    }

    @NotNull
    @Override
    public String[] getVariants() {
        return VARIANTS;
    }

    @NotNull
    @Override
    public String[] getExtraVariants() {
        return MODELS;
    }
}
