package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.libs.StringObfuscator;
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

public class ItemSwordAssembly extends ItemCADComponent implements ICADAssembly {

    public static final String FLUGEL_TIARA_II_ELECTRIC_BOOGALOO = "53839016453E46E15E5DE8F8CE7AE737843767E7CEDAAC2D91F67E44C46CC513";

    private final String model;
    public ItemSwordAssembly(Item.Properties props, String model) {
        super(props);
        this.model = model;
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
        if(false) {
            addStat(EnumCADStat.EFFICIENCY, 90);
            addStat(EnumCADStat.POTENCY, 350);
        }
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
