package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.libs.StringObfuscator;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADAssembly;


import java.util.List;

public class ItemSwordAssembly extends ItemAdvComponent implements ICADAssembly {

    public static final String FLUGEL_TIARA_II_ELECTRIC_BOOGALOO = "53839016453E46E15E5DE8F8CE7AE737843767E7CEDAAC2D91F67E44C46CC513";

    public ItemSwordAssembly (String name){
        super(name, VARIANTS);
    }

    public static final String[] VARIANTS = {
            "sword/sword_assembly_iron",
            "sword/sword_assembly_gold",
            "sword/sword_assembly_psimetal",
            "sword/sword_assembly_ebony_psimetal",
            "sword/sword_assembly_ivory_psimetal",
            "sword/sword_assembly_creative",
            "igalima",
    };
    public static final String[] MODELS = {
            "sword/sword_iron",
            "sword/sword_gold",
            "sword/sword_psimetal",
            "sword/sword_ebony_psimetal",
            "sword/sword_ivory_psimetal",
            "sword/sword_creative",
            "igalima",
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

        // Hey stop looking at my code
        addStat(EnumCADStat.EFFICIENCY, 6, 90);
        addStat(EnumCADStat.POTENCY, 6, 350);
    }

    @Override
    public ItemStack createCADStack(ItemStack stack, List<ItemStack> allComponents) {
        if (StringObfuscator.matchesHash(stack.getTranslationKey()+stack.getDisplayName(),FLUGEL_TIARA_II_ELECTRIC_BOOGALOO)) {
            stack.setDamage(6);
            stack.getTag().remove("display");
        }
        return ItemSwordCad.makeCAD(allComponents);
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
