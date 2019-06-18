package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.teamwizardry.librarianlib.core.client.ModelHandler;
import com.teamwizardry.librarianlib.features.base.IVariantHolder;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADAssembly;

import java.util.List;

public class ItemSwordAssembly extends ItemAdvComponent implements ICADAssembly, IVariantHolder {
    public ItemSwordAssembly (String name){
        super(name, VARIANTS);
    }

    public static final String[] VARIANTS = {
            "sword_assembly_iron",
            "sword_assembly_gold",
            "sword_assembly_psimetal",
            "sword_assembly_ebony_psimetal",
            "sword_assembly_ivory_psimetal",
            "sword_assembly_creative"
    };
    public static final String[] MODELS = {
            "sword_iron",
            "sword_gold",
            "sword_psimetal",
            "sword_ebony_psimetal",
            "sword_ivory_psimetal",
            "sword_creative"
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
    @NotNull
    @Override
    public String[] getVariants() {
        return VARIANTS;
    }

    @Override
    public ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        return ItemSwordCad.makeCAD(allComponents);
    }

    @Override
    public ModelResourceLocation getCADModel(ItemStack stack, ItemStack cad) {

        return ModelHandler.INSTANCE.getResource(Psipherals.MODID, MODELS[Math.min(MODELS.length - 1, stack.getItemDamage())]);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.ASSEMBLY;
    }
}
