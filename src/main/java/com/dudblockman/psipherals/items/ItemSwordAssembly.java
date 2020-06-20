package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.libs.ItemNames;
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
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.lib.LibMisc;

import java.util.List;

public class ItemSwordAssembly extends ItemCADComponent implements ICADAssembly {

    public static final String FLUGEL_TIARA_II_ELECTRIC_BOOGALOO = "9808f8f56ab3a6ef4d07dd9adb55a540604447451ba81f2d2919b4b6b2664798";

    private final String model;
    public ItemSwordAssembly(Item.Properties props, String model) {
        super(props);
        this.model = model;
    }

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
